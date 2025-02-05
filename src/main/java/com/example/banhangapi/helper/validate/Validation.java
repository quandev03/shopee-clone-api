package com.example.banhangapi.helper.validate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Validation {
    ValidType validType;
    String textCheck;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,20}$";
    private static final String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    private static final String phonePattern = "^(03|05|07|08|09)[0-9]{8}$";
    public ResponseValidation validate( ) {
        switch ( validType ) {
            case USERNAME -> {
                if (textCheck == null) {
                    return new ResponseValidation(false, "Username is required!");
                } else if (textCheck.matches(USERNAME_PATTERN)) {
                    return new ResponseValidation(true, "Username is valid!");
                }else {
                    return new ResponseValidation(false, "Username is not valid!");
                }
            }
            case PASSWORD -> {
                if (textCheck.length() <8) {
                    return new ResponseValidation(false, "Password is too short!");
                } else if (textCheck.matches(passwordPattern)) {
                    return new ResponseValidation(true, "Password is valid!");
                } else {
                    return new ResponseValidation(false, "Password is incorrect!");
                }
            }
            case PHONE -> {
                if (textCheck.length() != 10) {
                    return new ResponseValidation(false, "Phone is incorrect!");
                } else if (textCheck.matches(phonePattern)) {
                    return new ResponseValidation(true, "Phone is valid!");
                } else {
                    return new ResponseValidation(false, "Phone is valid!");
                }
            }
            default -> {
                return new ResponseValidation(false, "Invalid type!");
            }
        }
    }

}
