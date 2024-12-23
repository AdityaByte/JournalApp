package com.journalapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.journalapp.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class SecurityConfig{

    private final UserDetailServiceImpl userDetailServiceImpl;

    SecurityConfig(UserDetailServiceImpl userDetailServiceImpl){
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(request -> request
        .requestMatchers("/journal/**" , "/user/**").authenticated() // ** - wildcard pattern
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll())
        .csrf(customizer -> customizer.disable())
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailServiceImpl).passwordEncoder(passwordEncoder());
    }   

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}