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
    @Size(min = 5, max = 20,message = "Invalid username length")
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "Username is invalid!")
    private String username;

    @Size(min = 8, max = 255, message = "Invalid password length")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Password is invalid")
    private String password;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Phone number is incorrect")
    @Size(min = 10, max = 10, message = "Phone number is incorrect")
    private String phoneNumber;

}
