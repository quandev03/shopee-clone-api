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
    private String nameProduce;
    private String description;
    private float price;
    private long quantity;
    private long soldQuantity;
    private long viewedQuantity;
    private List<CommentDTO> comments;
    List<ImageResponseDTO> image;

}
