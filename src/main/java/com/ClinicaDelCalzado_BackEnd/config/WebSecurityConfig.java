package com.ClinicaDelCalzado_BackEnd.config;

import com.ClinicaDelCalzado_BackEnd.dtos.enums.AdminTypeEnum;
import com.ClinicaDelCalzado_BackEnd.exceptions.JWTAuthorizationException;
import com.ClinicaDelCalzado_BackEnd.security.JWTAuthenticationFilter;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/ping").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/admins/created").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/admins/{adminId}").hasRole(AdminTypeEnum.PRINCIPAL.getValue())
                        .requestMatchers(HttpMethod.PUT,"/api/v1/admins/updated/{adminId}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/admins/list").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/admins/password/{adminId}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/questions/list").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/work-orders/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/work-orders/cancel/{orderNumber}").hasAnyAuthority(AdminTypeEnum.PRINCIPAL.getKeyName())
                        .requestMatchers(HttpMethod.PUT,"/api/v1/work-orders/payment/{orderNumber}").hasAnyAuthority(AdminTypeEnum.PRINCIPAL.getKeyName(), AdminTypeEnum.SECONDARY.getKeyName())
                        .requestMatchers(HttpMethod.PUT,"/api/v1/work-orders/comment/**").hasAnyAuthority(AdminTypeEnum.PRINCIPAL.getKeyName(), AdminTypeEnum.SECONDARY.getKeyName())
                        .requestMatchers(HttpMethod.GET, "/api/v1/company/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/work-orders/**").hasAnyAuthority(AdminTypeEnum.PRINCIPAL.getKeyName(), AdminTypeEnum.SECONDARY.getKeyName())
                        .requestMatchers(HttpMethod.GET,"/api/v1/reports/**").hasAnyAuthority(AdminTypeEnum.PRINCIPAL.getKeyName(), AdminTypeEnum.SECONDARY.getKeyName())
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.exceptionHandling(exh -> exh.authenticationEntryPoint(jwtAuthorizationException));
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
