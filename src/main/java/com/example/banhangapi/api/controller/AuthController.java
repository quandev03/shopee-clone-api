package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.request.*;
import com.example.banhangapi.api.service.implement.UserServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@CrossOrigin("localhost:3000")
public class AuthController {
    @Autowired
    UserRepository userRepositoty;

    @Autowired
    UserServiceImple userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Page<User> user(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepositoty.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@RequestBody RequestRegister user) {
        return ResponseEntity.ok(this.userService.registerAccount(user));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> loginAccount(@RequestBody RequestLogin user) {
        return this.userService.loginAccount(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@RequestBody RequestUpdate user) {
        return this.userService.updateInfoUser(user);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestParam String id) {
        return this.userService.deleteUser(id);
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
    public ResponseEntity<?> infoAccount(@RequestParam String idUser) {
        return this.userService.infoAccount(idUser);
    }

    @GetMapping("/info-me")
    public ResponseEntity<Object> infoMe(){
        return ResponseEntity.ok(this.userService.infoMe());
    }

    @PutMapping("/update-avatar")
    public ResponseEntity<Object> updateAvatar( @RequestBody MultipartFile image) {
        userService.updateAvatar(image);
        return ResponseEntity.ok("Success");
    }
}
