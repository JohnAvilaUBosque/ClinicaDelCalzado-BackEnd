package com.ClinicaDelCalzado_BackEnd.config;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminTypeEnum;
import com.ClinicaDelCalzado_BackEnd.exceptions.JWTAuthorizationException;
import com.ClinicaDelCalzado_BackEnd.securty.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JWTAuthorizationException jwtAuthorizationException;

    @Autowired
    public WebSecurityConfig(JWTAuthorizationException jwtAuthorizationException) {
        this.jwtAuthorizationException = jwtAuthorizationException;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/ping").denyAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/admins/created").hasRole(AdminTypeEnum.PRINCIPAL.getValue())
                        .requestMatchers(HttpMethod.GET,"/api/v1/admins/{adminId}").hasRole(AdminTypeEnum.PRINCIPAL.getValue())
                        .requestMatchers(HttpMethod.PUT,"/api/v1/admins/updated/{adminId}").hasRole(AdminTypeEnum.PRINCIPAL.getValue())
                        .requestMatchers(HttpMethod.GET,"/api/v1/admins/list").hasRole(AdminTypeEnum.PRINCIPAL.getValue())
                        .requestMatchers(HttpMethod.PUT,"/api/v1/admins/updated/status/{adminId}").hasRole(AdminTypeEnum.PRINCIPAL.getValue())
                        .requestMatchers(HttpMethod.GET,"/api/v1/questions/list").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/work-orders/created").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.exceptionHandling(exh -> exh.authenticationEntryPoint(jwtAuthorizationException));
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
