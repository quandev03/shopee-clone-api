package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.ResponseDto;
import com.example.banhangapi.api.dto.UserResponseDTO;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.request.*;
import com.example.banhangapi.api.service.UserService;
import com.example.banhangapi.helper.exception.ResponseValidate;
import com.example.banhangapi.security.JwtService;
import com.example.banhangapi.grpc.GrpcClientService;
import com.example.banhangapi.helper.exception.ValidationUtil;
import com.example.banhangapi.kafka.MessageProducer;
import com.example.banhangapi.redis.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImple implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    MessageProducer massageProducer;
    @Autowired
    GrpcClientService grpcClientService;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;


    public UserDetails loadUserByUsername(String username) {
        Specification<User> spec = Specification.where(UserSpecification.hasUsername(username));
        List<User> user = userRepository.findAll(spec);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user.get(0));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(()->new RuntimeException("You are not logged in, please login first"));
    }

    @SneakyThrows
    public User RegisterAccount(RequestRegister requestRegister) {
        ResponseValidate responseValidate = new ValidationUtil<RequestRegister>().getMessage( requestRegister);
        if (!responseValidate.isValid()) {
            throw new RuntimeException(responseValidate.getMessages().toString());
        }
        if (userRepository.existsByUsername(requestRegister.getUsername())) {
            throw new RuntimeException("Username already exists");
        } else if (userRepository.existsByPhoneNumber(requestRegister.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        } else {
            User newUser = modelMapper.map(requestRegister, User.class);
            newUser.setPassword( passwordEncoder.encode(requestRegister.getPassword()));
            try {
                User user1 = userRepository.save(newUser);
                massageProducer.sendMessage("create_account", user1.getId().toString());
                return user1;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResponseEntity<?> LoginAccount (RequestLogin requestLogin) {
        try {
            ResponseValidate responseValidate = new ValidationUtil<RequestLogin>().getMessage( requestLogin);
            if (!responseValidate.isValid()) {
                return new ResponseEntity<>(responseValidate.getMessages(), HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(requestLogin.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed, Account does not exist"));
            if (passwordEncoder.matches(requestLogin.getPassword(), user.getPassword())) {
                String keyToken = "key_token_"+ user.getUsername();
                String refreshToken = jwtService.createRefreshToken(user.getUsername());
                redisService.saveData(keyToken, refreshToken, 302400100);
                ResponseDto responseDto = new ResponseDto(user.getUsername(), jwtService.generateToken(user.getUsername()), refreshToken, user.getRoles(), null, user.getId());
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Login failed, Password is incorrect", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed, Account does not exist", HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    public ResponseEntity<?> updateInfoUser(@RequestBody RequestUpdate requestUpdate) {
        ResponseValidate responseValidate = new ValidationUtil<RequestUpdate>().getMessage( requestUpdate);
        if (!responseValidate.isValid()) {
            return new ResponseEntity<>(responseValidate.getMessages(), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
            user.setAddress(requestUpdate.getAddress());
            user.setPhoneNumber(requestUpdate.getPhone());
            userRepository.save(user);
            massageProducer.sendMessage("update_account", user.getId().toString());
            return new ResponseEntity<>("Update successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Update failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try{
            userRepository.delete(user);
            return new ResponseEntity<>("Delete successful", HttpStatus.OK);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    public ResponseEntity<?> searchUserByUsername(RequestSearch requestSearch) {
        return new ResponseEntity<>(
                parseUserResponseDTOList(grpcClientService.searchUser(requestSearch)),
                HttpStatus.OK
        );
    }

    private static List<UserResponseDTO> parseUserResponseDTOList(String input) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(input, new TypeReference<List<UserResponseDTO>>(){});
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<?> refreshToken(RequestRefreshToken requestRefreshToken) {
        try{
            String keyToken = "key_token_"+ requestRefreshToken.getUsername();
            String refreshToken = (String) this.redisService.getData(keyToken);
            if (this.jwtService.validateFreshToken(refreshToken ) && refreshToken.equals(requestRefreshToken.getRefreshToken())) {
                return ResponseEntity.ok(this.jwtService.generateToken(this.jwtService.extractUserName(refreshToken)));
            }
            else {
                return new ResponseEntity<>("Refresh Token invalid",HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> resetPassword(RequestResetPass requestResetPass) {
        try{
        ResponseValidate responseValidate = new ValidationUtil<RequestResetPass>().getMessage( requestResetPass);
        if (!responseValidate.isValid()) {
            return new ResponseEntity<>(responseValidate.getMessages(), HttpStatus.UNAUTHORIZED);
        }

            User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new RuntimeException("Not found"));
            if (passwordEncoder.matches(user.getPassword(), requestResetPass.getOldPassword())) {
                if(!requestResetPass.getOldPassword().equals(requestResetPass.getNewPassword())){
                    String password = passwordEncoder.encode(requestResetPass.getNewPassword());
                    user.setPassword(password);
                    userRepository.save(user);
                    return new ResponseEntity<>("Reset password success" ,HttpStatus.OK);
                }else{
                    return new ResponseEntity<>( "Reset password fail, The new password must not be the same as the old password",HttpStatus.UNAUTHORIZED);
                }
            }else {
                return new ResponseEntity<>("Password invalid" ,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<?> infoAccount(Long idUser){
        try{
            User user = userRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            UserResponseDTO userDTO = modelMapper.map(user, UserResponseDTO.class);
            return new ResponseEntity<UserResponseDTO>(userDTO, HttpStatus.OK);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
