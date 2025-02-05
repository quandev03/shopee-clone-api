package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.request.*;
import com.example.banhangapi.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserRepository userRepositoty;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Page<User> user(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepositoty.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RequestRegister user) {
        return this.userService.RegisterAccount(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> loginAccount(@RequestBody RequestLogin user) {
        return this.userService.LoginAccount(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@RequestBody RequestUpdate user) {
        return this.userService.updateInfoUser(user);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        return this.userService.deleteUser(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchUser(@RequestBody RequestSearch requestSearch) {
        return this.userService.searchUserByUsername(requestSearch);
    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> refreshToken(@RequestBody RequestRefreshToken requestRefreshToken) {
        return this.userService.refreshToken(requestRefreshToken);
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> resetPassword(@RequestBody RequestResetPass requestResetPass) {
        return this.userService.resetPassword(requestResetPass);
    }

    @RequestMapping(value = "/info-user", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> infoAccount(@RequestParam Long idUser) {
        return this.userService.infoAccount(idUser);
    }

}
