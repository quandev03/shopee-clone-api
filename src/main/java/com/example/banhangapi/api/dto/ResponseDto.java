package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.globalEnum.ROLES;
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
    private ROLES roles;
    private String avatar;
    private String id;
}
