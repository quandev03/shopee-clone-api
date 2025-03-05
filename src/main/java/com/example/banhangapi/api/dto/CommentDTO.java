package com.example.banhangapi.api.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private String content;
    private UserDto createBy;
    private Date createTime;
    private Date updateTime;
}
