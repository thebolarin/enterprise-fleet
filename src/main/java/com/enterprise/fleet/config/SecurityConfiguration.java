package com.enterprise.fleet.config;

import com.enterprise.fleet.exception.CustomAccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.enterprise.fleet.user.types.Permission.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                //users guarded endpoint
                                .requestMatchers(GET, "/api/v1/user").hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(PUT, "/api/v1/user").hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, "/api/v1/user").hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(POST, "/api/v1/user/customer").hasAnyAuthority(ADMIN_CREATE.getPermission(), SALES_REP_CREATE.getPermission())
                                .requestMatchers(POST, "/api/v1/vehicle").hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, "/api/v1/vehicle").hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, "/api/v1/vehicle").hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(POST, "/api/v1/vehicle-rental/sales-rep").hasAnyAuthority(ADMIN_CREATE.getPermission(), SALES_REP_CREATE.getPermission())
                                .requestMatchers(PUT, "/api/v1/vehicle-rental").hasAnyAuthority(ADMIN_UPDATE.getPermission(), SALES_REP_UPDATE.getPermission())
                                .requestMatchers(DELETE, "/api/v1/vehicle-rental").hasAnyAuthority(ADMIN_DELETE.getPermission(), SALES_REP_DELETE.getPermission())
                                .requestMatchers(POST, "/api/v1/location").hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, "/api/v1/location").hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, "/api/v1/location").hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(POST, "/api/v1/invoice").hasAnyAuthority(ADMIN_CREATE.getPermission(), SALES_REP_CREATE.getPermission())
                                .requestMatchers(PUT, "/api/v1/invoice").hasAnyAuthority(ADMIN_UPDATE.getPermission(), SALES_REP_UPDATE.getPermission())
                                .requestMatchers(DELETE, "/api/v1/invoice").hasAnyAuthority(ADMIN_DELETE.getPermission(), SALES_REP_DELETE.getPermission())
                                .requestMatchers(POST, "/api/v1/transaction").hasAnyAuthority(ADMIN_CREATE.getPermission(), SALES_REP_CREATE.getPermission())
                                .requestMatchers(PUT, "/api/v1/transaction").hasAnyAuthority(ADMIN_UPDATE.getPermission(), SALES_REP_UPDATE.getPermission())
                                .requestMatchers(DELETE, "/api/v1/transaction").hasAnyAuthority(ADMIN_DELETE.getPermission(), SALES_REP_DELETE.getPermission())

                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer -> configurer
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }
}
