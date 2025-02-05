package com.example.banhangapi.api.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestUpdate {

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Phone number is incorrect")
    @Size(min = 10, max = 10, message = "Phone number is incorrect")
    private String phone;

    private String address;

}
