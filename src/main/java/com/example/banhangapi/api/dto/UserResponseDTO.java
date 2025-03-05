package com.example.banhangapi.api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String address;
    private String phoneNumber;
}
