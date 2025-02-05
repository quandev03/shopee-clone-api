package com.example.banhangapi.api.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RequestSearch {
    private String keyword;
}
