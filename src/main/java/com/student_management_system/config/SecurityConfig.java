package com.student_management_system.config;

import com.student_management_system.filters.CustomAuthenticationFilter;
import com.student_management_system.filters.CustomAuthorizationFilter;
import com.student_management_system.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(), jwtSecret);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/users/login");
        return http
                .cors(Customizer.withDefaults())
                .csrf( csrf -> csrf.disable())
                .authorizeRequests( auth -> {
                    auth.antMatchers(GET, "/api/v1/users/**").hasAnyRole("ADMIN", "USER");
                    auth.antMatchers(GET, "/api/v1/students/**").hasAnyRole("ADMIN", "USER");
                    auth.antMatchers(POST, "/api/v1/students/**").hasRole("ADMIN");
                    auth.antMatchers(PUT, "/api/v1/students/**").hasRole("ADMIN");
                    auth.antMatchers(DELETE, "/api/v1/students/**").hasRole("ADMIN");
                    auth.antMatchers(POST, "/api/v1/users/**").hasRole("ADMIN");
                    auth.antMatchers(PUT, "/api/v1/users/**").hasRole("ADMIN");
                    auth.antMatchers(DELETE, "/api/v1/users/**").hasRole("ADMIN");
                    auth.antMatchers("/api/v1/users/login").permitAll();
                    auth.antMatchers("/api/v1/users/logout").permitAll();
                    auth.antMatchers("/").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement( session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .logout( logout -> {
                    logout.logoutUrl("/api/v1/users/logout");
                    logout.deleteCookies("SCHOOL_SYSTEM");
                    logout.logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
                })
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(jwtSecret), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "https://dev746.dxm49vk3agg.amplifyapp.com",
                "https://www.school-management-system.link",
                "https://school-management-system.link"
                ));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

}
