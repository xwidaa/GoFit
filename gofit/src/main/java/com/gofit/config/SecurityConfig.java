package com.gofit.config;

import com.gofit.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Ensure CORS is handled
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Public resources
                        .requestMatchers(
                                "/auth/**",
                                "/index.html",
                                "/dashboard.html",
                                "/admin_dashboard.html",
                                "/workouts.html",
                                "/nutrition.html",
                                "/**.css",
                                "/**.js",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        // 2. Workouts & Nutrition (Specific access)
                        .requestMatchers("/workouts/**").hasAnyAuthority("USER", "ADMIN", "CLIENT")
                        .requestMatchers("/api/nutrition/**").hasAnyAuthority("USER", "ADMIN", "CLIENT")

                        // 3. Admin Specific Routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 4. User Specific Routes
                        .requestMatchers("/user/me").authenticated()
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // 5. Secure everything else
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable());

        return http.build();
    }

    // Adding a basic CORS configuration to prevent browser blocking
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // For development; restrict this in production
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}