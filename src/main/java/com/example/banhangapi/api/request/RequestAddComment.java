package com.example.banhangapi.api.request;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class RequestAddComment {
    private String content;
}
