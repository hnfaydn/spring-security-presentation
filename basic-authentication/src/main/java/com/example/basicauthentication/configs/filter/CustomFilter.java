package com.example.basicauthentication.configs.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomFilter extends GenericFilterBean {

  private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    UsernamePasswordAuthenticationToken authRequest =
        this.authenticationConverter.convert(httpServletRequest);
    final var name = authRequest.getName();
    final var method = httpServletRequest.getMethod();
    log.info("Following user:'{}' has sent request with method type of {}", name, method);

    chain.doFilter(request, response);
  }
}
