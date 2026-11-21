package com.example.demo4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())   // 新增这一行
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/delete").hasAnyRole("ADMIN")
                        .requestMatchers("/add", "/update", "/students").authenticated()
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login.html")
                        .loginProcessingUrl("/dologin")
                        .defaultSuccessUrl("/index.html",true)
                        .permitAll());
        return http.build();
    }


    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}123456")
                .roles("ADMIN")
                .build();
        UserDetails user=User.withUsername("user")
                .password("{noop}123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin,user);
    }
}
