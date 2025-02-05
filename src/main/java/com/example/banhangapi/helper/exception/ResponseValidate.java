package com.example.banhangapi.helper.exception;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseValidate {
    private boolean isValid;
    private List<String> messages;
}
