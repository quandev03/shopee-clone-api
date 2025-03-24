package com.example.banhangapi.config;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.service.implement.UserServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<User> {


    @Autowired
    private UserServiceImple userServiceImple;
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of( userServiceImple.getCurrentUser());  // Trả về null nếu không xác thực
    }
}