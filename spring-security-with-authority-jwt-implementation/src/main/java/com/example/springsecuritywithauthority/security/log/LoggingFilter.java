package com.example.springsecuritywithauthority.security.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Enumeration;

@Configuration
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
  private Long duration;
  private Instant requestIncomeTime;
  private Instant requestEndTime;


  @Override
  protected void doFilterInternal(
      final HttpServletRequest httpServletRequest,
      final HttpServletResponse httpServletResponse,
      final FilterChain filterChain)
      throws ServletException, IOException {
    requestIncomeTime = Instant.now();
    final ContentCachingRequestWrapper requestWrapper =
            new ContentCachingRequestWrapper(httpServletRequest);
    final ContentCachingResponseWrapper responseWrapper =
            new ContentCachingResponseWrapper(httpServletResponse);

    filterChain.doFilter(requestWrapper, responseWrapper);

    requestWrapper.getInputStream().readAllBytes();
    final var requestBody = getContentAsString(requestWrapper.getContentAsByteArray());
    logRequest(httpServletRequest, requestBody);

    final var responseBody = getContentAsString(responseWrapper.getContentAsByteArray());
    logResponse(httpServletRequest, httpServletResponse, responseBody);
    responseWrapper.copyBodyToResponse();

  }

  private String getContentAsString(byte[] contentAsByteArray) {
    return new String(contentAsByteArray, StandardCharsets.UTF_8);
  }

  private void logRequest(final HttpServletRequest httpServletRequest, final String requestBody) {
    final StringBuilder requestStringBuilder = new StringBuilder();
    requestStringBuilder.append("\n").append("REQUEST: ").append("\n");
    requestStringBuilder
        .append(httpServletRequest.getMethod())
        .append(" ")
        .append(httpServletRequest.getRequestURL());
    if (httpServletRequest.getParameterNames().hasMoreElements()) {
      requestStringBuilder.append(requestParametersQuery(httpServletRequest));
    }
    requestStringBuilder.append(" ").append(httpServletRequest.getProtocol());
    requestStringBuilder.append("\n");
    requestStringBuilder.append(requestHeaders(httpServletRequest));
    requestStringBuilder.append("\n");
    requestStringBuilder.append(requestBody);
    log.info(requestStringBuilder.toString());
  }

  public void logResponse(
      final HttpServletRequest httpServletRequest,
      final HttpServletResponse httpServletResponse,
      final String responseBody) {
    final StringBuilder responseStringBuilder = new StringBuilder();
    responseStringBuilder.append("\n").append("RESPONSE: ").append("\n");
    responseStringBuilder
        .append(httpServletRequest.getMethod())
        .append(" ")
        .append(httpServletRequest.getRequestURL());
    if (httpServletRequest.getParameterNames().hasMoreElements()) {
      responseStringBuilder.append(requestParametersQuery(httpServletRequest));
    }
    responseStringBuilder.append(" ").append(httpServletRequest.getProtocol()).append("\n");
    responseStringBuilder
        .append("Status Code: ")
        .append(httpServletResponse.getStatus())
        .append("\n");
    responseStringBuilder.append(responseHeaders(httpServletResponse));
    requestEndTime = Instant.now();
    duration = Duration.between(requestIncomeTime, requestEndTime).toMillis();
    responseStringBuilder
        .append("Request Time Duration: ")
        .append(duration)
        .append(" ms")
        .append("\n");
    if (responseBody.isEmpty()) {
      responseStringBuilder.append("<Response body is empty>");
    } else {
      responseStringBuilder.append("Response Body:").append("\n").append(responseBody);
    }
    log.info(responseStringBuilder.toString());
  }

  private String requestParametersQuery(final HttpServletRequest httpServletRequest) {
    final Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
    final StringBuilder parameterBuilder = new StringBuilder();
    parameterBuilder.append("?");
    while (parameterNames.hasMoreElements()) {
      final String key = parameterNames.nextElement();
      final String value = httpServletRequest.getParameter(key);
      parameterBuilder.append(key).append("=").append(value);
      if (parameterNames.hasMoreElements()) {
        parameterBuilder.append("&");
      }
    }
    return parameterBuilder.toString();
  }

  private String requestHeaders(final HttpServletRequest request) {
    final Enumeration<String> headerNames = request.getHeaderNames();
    final StringBuilder requestHeaders = new StringBuilder();
    while (headerNames.hasMoreElements()) {
      final String key = headerNames.nextElement();
      final String value = request.getHeader(key);
      requestHeaders.append(key).append(": ").append(value).append("\n");
    }
    return requestHeaders.toString();
  }

  private String responseHeaders(final HttpServletResponse response) {
    final Collection<String> headerNames = response.getHeaderNames();
    final StringBuilder responseHeaders = new StringBuilder();
    for (final String header : headerNames) {
      final var responseHeader = response.getHeader(header);
      responseHeaders.append(header).append(": ").append(responseHeader).append("\n");
    }
    return responseHeaders.toString();
  }
}
