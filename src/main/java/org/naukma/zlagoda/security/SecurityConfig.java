package org.naukma.zlagoda.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
//                                .requestMatchers("/login").permitAll()
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
//                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(login -> login.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }
}
