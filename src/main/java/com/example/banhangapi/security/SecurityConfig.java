package com.example.banhangapi.security;


import com.example.banhangapi.api.service.implement.UserServiceImple;
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
                                        "auth/login", "auth/register", "product/search", "product/get-list", "product/dataProduct","auth/refresh-token", "/address-manage/*",
                                        "api-admin-manager/get-data-voucher", "api-admin-manager/get-list-voucher-can-apply", "product/get-list-category"
                                ).permitAll()
                                .requestMatchers("auth/search","auth/user", "auth/update", "auth/reset-password", "comment/delete-comment", "comment/add-comment", "comment/edit-comment",
                                        "address-manage/add-new-my-address", "/cart-order/add-product-in-cart", "cart-order/get-data-cart"
                                ).authenticated()
                                .requestMatchers(
                                        "/product/create",
                                        "product/update", "product/delete",
                                        "auth/delete", "api-admin-manager/*", "product/upload-image"
                                ).hasRole("ADMIN")
                         )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless
                .authenticationProvider(authenticationProvider()) // Register the authentication provider
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter before processing the request
                .build();
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
}