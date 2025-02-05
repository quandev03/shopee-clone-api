package com.example.banhangapi.helper.validate;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseValidation {
    private boolean isValid;
    private String message;
}
