package com.example.banhangapi.api.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class RequestRegister {

    // Các điều kiện kiểm tra mật khẩu
    private static final String MIN_LENGTH_PATTERN = ".{8,}"; // Mật khẩu ít nhất 8 ký tự
    private static final String CONTAINS_UPPERCASE_PATTERN = "(?=.*[A-Z])"; // Chứa ít nhất 1 chữ cái viết hoa
    private static final String CONTAINS_LOWERCASE_PATTERN = "(?=.*[a-z])"; // Chứa ít nhất 1 chữ cái viết thường
    private static final String CONTAINS_NUMBER_PATTERN = "(?=.*\\d)"; // Chứa ít nhất 1 chữ số
    private static final String CONTAINS_SPECIAL_CHAR_PATTERN = "(?=.*[@#$%^&+=!])"; // Chứa ít nhất 1 ký tự đặc biệt

    @Size(min = 5, max = 20,message = "Username không hợp lệ, username phải có độ dài từ 5-20 kí tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]$", message = "Username phải có dạng a-zA-Z0-9")
    private String username;

    @Size(min = 8, max = 255, message = "Độ dài của mật khẩu không hợp lệ, tối thiểu 8 kí tự")
    @Pattern(regexp = MIN_LENGTH_PATTERN, message = "Mật khẩu phải có ít nhất 8 ký tự")
    @Pattern(regexp = CONTAINS_UPPERCASE_PATTERN, message = "Mật khẩu phải chứa ít nhất một chữ cái viết hoa")
    @Pattern(regexp = CONTAINS_LOWERCASE_PATTERN, message = "Mật khẩu phải chứa ít nhất một chữ cái viết thường")
    @Pattern(regexp = CONTAINS_NUMBER_PATTERN, message = "Mật khẩu phải chứa ít nhất một chữ số")
    @Pattern(regexp = CONTAINS_SPECIAL_CHAR_PATTERN, message = "Mật khẩu phải chứa ít nhất một ký tự đặc biệt (@#$%^&+=!)")

    private String password;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Đầu số thuê bao không hợp lệ")
    @Size(min = 10, max = 10, message = "Độ dài không hợp lệ, số điện thoại phải là số cá nhân(10 chứ số)")
    private String phoneNumber;

}
