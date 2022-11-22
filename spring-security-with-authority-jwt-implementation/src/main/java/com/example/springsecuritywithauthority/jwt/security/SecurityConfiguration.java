package com.example.springsecuritywithauthority.jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Locale;

/*
The prePostEnabled property enables Spring Security pre/post annotations.
The securedEnabled property determines if the @Secured annotation should be enabled.
The jsr250Enabled property allows us to use the @RoleAllowed annotation.
*/

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
    private final JwtUserDetailsConverter jwtUserDetailsConverter;

    public SecurityConfiguration(JwtUserDetailsConverter jwtUserDetailsConverter) {
        this.jwtUserDetailsConverter = jwtUserDetailsConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorize ->
                                authorize
                                        //  public endpoint everyone can reach without any authority
                                        .antMatchers("/h2-console", "/api/user/register")
                                        .permitAll()

                                        //  admin endpoint only admin authority can reach
                                        // .antMatchers("/api/user/admin-login")
                                        // .hasAuthority(Authority.ADMIN.getAuthority().toUpperCase(Locale.ENGLISH))

                                        //  user endpoint only user authority can reach
                                        // .antMatchers("/api/user/user-login")
                                        // .hasAuthority(Authority.USER.getAuthority().toUpperCase(Locale.ENGLISH))

                                        //  editor endpoint only editor authority can reach
                                        // .antMatchers("/api/user/editor-login")
                                        // .hasAuthority(Authority.EDITOR.getAuthority().toUpperCase(Locale.ENGLISH))

                                        //  any authority can reach
                                        .antMatchers("/api/user/any-of-request-login")
                                        .hasAnyAuthority(
                                                Authority.ADMIN.getAuthority().toUpperCase(Locale.ENGLISH),
                                                Authority.USER.getAuthority().toUpperCase(Locale.ENGLISH),
                                                Authority.EDITOR.getAuthority().toUpperCase(Locale.ENGLISH),
                                                Authority.READONLY.getAuthority().toUpperCase(Locale.ENGLISH)))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(
                        httpSecurityOAuth2ResourceServerConfigurer ->
                                httpSecurityOAuth2ResourceServerConfigurer
                                        .jwt()
                                        .jwtAuthenticationConverter(jwtUserDetailsConverter))
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
