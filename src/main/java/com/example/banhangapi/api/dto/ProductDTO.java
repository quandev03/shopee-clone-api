package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.entity.Image;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO implements Serializable {
    private String id;
    private String nameProduct;
    private String description;
    private float price;
    private long quantity;
    private long soldQuantity;
    private long viewedQuantity;
    private List<String> images;
    private String image;
    private CategoryResponseDTO category;



}
