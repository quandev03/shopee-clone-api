package com.example.banhangapi.security;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.config.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@Configuration
//@EnableJpaAuditing
//public class JpaConfig {
//
//    @Bean
//    public AuditorAware<User> auditorProvider() {
//        return new AuditorAwareImpl(); // Cung cấp đối tượng User từ AuditorAwareImpl
//    }
//}
