package com.example.banhangapi.api.request;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestLogin {

    @Size(min = 5, max = 20,message = "Độ dài tên đăng nhập không hợp lệ")
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "Tên đăng nhập không hợp lệ")
    private String username;

    @Size(min = 8, max = 255, message = "Độ dài mật khẩu không hợp lệ")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Mật khẩu không hợp lệ")
    private String password;
}
