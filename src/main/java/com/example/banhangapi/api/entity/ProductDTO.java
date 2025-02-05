package com.example.banhangapi.api.entity;

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
    private Long id;
    private String nameProduce;
    private String description;
    private float price;
    private long quantity;
    private long soldQuantity;
    private long viewedQuantity;
    private List<CommentDTO> comments;

}
