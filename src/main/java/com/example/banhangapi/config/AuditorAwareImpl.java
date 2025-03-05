package com.example.banhangapi.config;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.service.UserService;
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
    private UserServiceImple userService;  // Inject UserService để lấy đối tượng User

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Lấy tên người dùng từ Authentication
        String username = authentication.getName();

        // Truy vấn User từ UserService dựa trên tên người dùng
        User user = userService.getCurrentUser();

        return Optional.ofNullable(user);  // Trả về đối tượng User
    }
}