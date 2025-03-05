package com.example.banhangapi.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImageResponseDTO implements Serializable {
    private String id;
    private String pathImage;
    private String description;
}
