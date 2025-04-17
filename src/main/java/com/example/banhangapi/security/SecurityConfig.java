package com.example.banhangapi.security;


import com.example.banhangapi.api.service.implement.UserServiceImple;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@CrossOrigin
public class SecurityConfig {

    @Autowired

    private JwtFilter jwtFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImple();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults()) // Apply CORS
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/auth/login", "/auth/register", "/product/search", "/product/get-list", "/product/dataProduct", "/auth/refresh-token", "/address-manage/*",
                                        "/api-admin-manager/get-data-voucher", "/api-admin-manager/get-list-voucher-can-apply", "/product/get-list-category"
                                ).permitAll()
                                .requestMatchers("auth/search","auth/user", "auth/update", "auth/reset-password", "comment/delete-comment", "comment/add-comment", "comment/edit-comment",
                                        "address-manage/add-new-my-address", "/cart-order/add-product-in-cart", "cart-order/get-data-cart", "product/get-data-cart",
                                        "/cart-order/update-cart", "cart-order/remove-cart", "/cart-order/create-order", "/address-manage/update-my-address", "/auth/info-me", "/auth/update-avatar",
                                        "/cart-order/get-order-by-status", "/product/get-list-category",
                                        "/api-admin-manager/get-list-voucher-can-apply", "/cart-order/rate-product"
                                ).authenticated()
                                .requestMatchers(
                                        "/product/create",
                                        "product/update", "product/delete",
                                        "auth/delete", "api-admin-manager/*", "product/upload-image",
                                        "/api-admin-manager/get-data-dashboard", "/api-admin-manager/get-all-voucher",
                                        "/api-admin-manager/get-all-product-mode-admin", "/product/create-new-category",
                                        "/api-admin-manager/decentralization-admin", "/api-admin-manager/decentralization-censorship",
                                        "/api-admin-manager/decentralization-user", "/api-admin-manager/lock-unlock",
                                        "/api-admin-manager/get-all-user-for-admin", "/api-admin-manager/get-order-admin",
                                        "/cart-order/change-status-order"
                                ).hasRole("ADMIN")
                         )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless
                .authenticationProvider(authenticationProvider()) // Register the authentication provider
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter before processing the request
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://34.46.178.207:3000", "http://localhost:3000", "https://shopee.quandev03.id.vn")); // ✅ frontend IP
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // nếu bạn dùng cookies, JWT

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    // Defines a PasswordEncoder bean that uses bcrypt hashing by default for password encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Defines an AuthenticationManager bean to manage authentication processes
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Tên API")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}