package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.*;
import com.example.banhangapi.api.entity.Order;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.globalEnum.ROLES;
import com.example.banhangapi.api.mapper.OrderMapper;
import com.example.banhangapi.api.mapper.UserMapper;
import com.example.banhangapi.api.repository.OrderRepository;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.request.*;
import com.example.banhangapi.api.service.AddressService;
import com.example.banhangapi.api.service.ImageService;
import com.example.banhangapi.api.service.OrderService;
import com.example.banhangapi.helper.exception.ResponseValidate;
import com.example.banhangapi.security.JwtService;
import com.example.banhangapi.helper.exception.ValidationUtil;
import com.example.banhangapi.redis.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImple implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserMapper userMapper;



    public UserDetails loadUserByUsername(String username) {
        Specification<User> spec = Specification.where(UserSpecification.hasUsername(username));
        List<User> user = userRepository.findAll(spec);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user.get(0));
    }

    public User getCurrentUser() {
        // Lấy đối tượng CustomUserDetails từ SecurityContext
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Trả về đối tượng User từ CustomUserDetails
        return customUserDetails.getUser();
    }

    @SneakyThrows
    public User registerAccount(RequestRegister requestRegister) {
        ResponseValidate responseValidate = new ValidationUtil<RequestRegister>().getMessage( requestRegister);
        if (!responseValidate.isValid()) {
            log.error("Lỗi dữ liệu đầu vào");
            responseValidate.getMessages().forEach(log::error);
            throw new RuntimeException(responseValidate.getMessages().get(0));
        }
        if (userRepository.existsByUsername(requestRegister.getUsername())) {
            log.error("Username already exists");
            throw new RuntimeException("Tài khoản đã tồn tại");
        } else if (userRepository.existsByPhoneNumber(requestRegister.getPhoneNumber())) {
            log.error("Phone number already exists");
            throw new RuntimeException("Số điện thoại đã tồn tại");
        } else {
            User newUser = modelMapper.map(requestRegister, User.class);
            newUser.setPassword( passwordEncoder.encode(requestRegister.getPassword()));
            try {
                User user1 = userRepository.save(newUser);
                return user1;
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public ResponseEntity<?> loginAccount(RequestLogin requestLogin) {
            ResponseValidate responseValidate = new ValidationUtil<RequestLogin>().getMessage( requestLogin);
            if (!responseValidate.isValid()) {
                log.error(responseValidate.getMessages().toString());
                return new ResponseEntity<>(responseValidate.getMessages(), HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findByUsername(requestLogin.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại, hoặc username không chính xác"));
            if(Boolean.FALSE.equals(user.getActive())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản đã bị khoá, vui lòng liên hệ quản trị viên để được hỗ trợ");
            }

            if (passwordEncoder.matches(requestLogin.getPassword(), user.getPassword())) {
                String refreshToken = jwtService.createRefreshToken(user.getUsername());
                String accessToken = jwtService.generateToken(user.getUsername());
                String localDateTime = LocalDate.now().toString();
                user.setLastLogin(localDateTime);
                userRepository.save(user);
                ResponseDto responseDto = new ResponseDto(user.getUsername(), accessToken, refreshToken, user.getRoles(), null, user.getId());
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Mật khẩu không chính xác", HttpStatus.UNAUTHORIZED);
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
            user.setPhoneNumber(requestUpdate.getPhone());
            user.setBirthday(requestUpdate.getBirthday());
            userRepository.save(user);
            return new ResponseEntity<>("Cập nhật thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cập nhật thất bại", HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try{
            userRepository.delete(user);
            return new ResponseEntity<>("Xoá thành công", HttpStatus.OK);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại");
        }
    }


    private static List<UserResponseDTO> parseUserResponseDTOList(String input) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(input, new TypeReference<List<UserResponseDTO>>(){});
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<String> refreshToken(RequestRefreshToken requestRefreshToken) {
        try{
            return ResponseEntity.ok(this.jwtService.generateToken(this.jwtService.extractUserName(requestRefreshToken.getRefreshToken())));
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
            if (passwordEncoder.matches( requestResetPass.getOldPassword(),user.getPassword())) {
                if(!requestResetPass.getOldPassword().equals(requestResetPass.getNewPassword())){
                    String password = passwordEncoder.encode(requestResetPass.getNewPassword());
                    user.setPassword(password);
                    userRepository.save(user);
                    return new ResponseEntity<>("Cập thật mật khẩu thành công" ,HttpStatus.OK);
                }else{
                    return new ResponseEntity<>( "Mật khẩu mới phải khác mật khẩu cũ",HttpStatus.UNAUTHORIZED);
                }
            }else {
                return new ResponseEntity<>("Mật khẩu không hợp lệ" ,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<?> infoAccount(String idUser){
        try{
            User user = userRepository.findById(idUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            UserResponseDTO userDTO = modelMapper.map(user, UserResponseDTO.class);
            return new ResponseEntity<UserResponseDTO>(userDTO, HttpStatus.OK);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public InforMeDTO infoMe(){
        InforMeDTO inforMeDTO = new InforMeDTO();
        User user = getCurrentUser();
        inforMeDTO.setUsername(user.getUsername());
        inforMeDTO.setBirthday(user.getBirthday());
        inforMeDTO.setAvatar(user.getAvatar());
        inforMeDTO.setId(user.getId());
        inforMeDTO.setPhone(user.getPhoneNumber());
        inforMeDTO.setFullName(user.getFullName());
        inforMeDTO.setAddressList(addressService.getListMyAddressForUser());
        return inforMeDTO;
    }

    @Transactional

    public void updateAvatar(MultipartFile avatarFile) {
        User currentUser = getCurrentUser();
        ImageServiceImpl.UploadFileDTO avatar = imageService.uploadFile(avatarFile);
        currentUser.setAvatar(avatar.getFile());
        try{
            userRepository.save(currentUser);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<UserAdminDTO> getUserAdmin(Pageable pageable, Boolean active, String username, String fullname, String phone) {

        Page<User> users = userRepository.findAllForAdmin(pageable, active, username, fullname, phone);
        return users.map(user -> userMapper.toUserAdminDTO(user));
    }

    public void decentralizationAdmin(String idUser) {
        User currentUser = getCurrentUser();
        if(Boolean.FALSE.equals(currentUser.getAdmin())){
            throw new RuntimeException("Bạn không có quyền quản trị viên");
        }
        userRepository.decentralizationAdmin(idUser, ROLES.ROLE_ADMIN);

    }
    public void decentralizationUser(String idUser) {
        User currentUser = getCurrentUser();
        if(Boolean.FALSE.equals(currentUser.getAdmin())){
            throw new RuntimeException("Bạn không có quyền quản trị viên");
        }
        userRepository.decentralizedUser(idUser, ROLES.ROLE_ADMIN);
    }
    public void decentralizationCensorship(String idUser) {
        User currentUser = getCurrentUser();
        if(Boolean.FALSE.equals(currentUser.getAdmin())){
            throw new RuntimeException("Bạn không có quyền quản trị viên");
        }
        userRepository.decentralizedCensorship(idUser, ROLES.ROLE_ADMIN);
    }
    public void lockAndUnlockAccount(String idUser) {
        User currentUser = getCurrentUser();
        if(Boolean.FALSE.equals(currentUser.getAdmin())){
            throw new RuntimeException("Bạn không có quyền quản trị viên");
        }
        userRepository.toggleAccountActive(idUser);
    }



}
