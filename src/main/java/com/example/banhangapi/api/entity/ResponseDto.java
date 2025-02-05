package com.example.banhangapi.api.entity;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    private String username;
    private String accessToken;
    private String refreshToken;

}
