package com.example.banhangapi.api.service;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.request.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    User RegisterAccount(RequestRegister requestRegister);
    ResponseEntity<?> LoginAccount (RequestLogin requestLogin);
    ResponseEntity<?> updateInfoUser(@RequestBody RequestUpdate requestUpdate);
    ResponseEntity<?> deleteUser(Long id);
    ResponseEntity<?> searchUserByUsername(RequestSearch requestSearch);
    ResponseEntity<?> refreshToken(RequestRefreshToken requestRefreshToken);
    ResponseEntity<?> resetPassword(RequestResetPass requestResetPass);
    ResponseEntity<?> infoAccount(Long idUser);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
