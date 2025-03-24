package com.example.banhangapi.api.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestRefreshToken {
    private String refreshToken;
}
