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

    @Size(min = 5, max = 20,message = "Invalid username length")
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "Username is invalid!")
    private String username;

    @Size(min = 8, max = 255, message = "Invalid password length")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "Password is invalid")
    private String password;
}
