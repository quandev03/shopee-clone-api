package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.entity.Comment;
import com.example.banhangapi.api.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "createBy", target = "createBy")
    CommentDTO toCommentDTO(Comment comment);

//    Comment toComment(CommentDTO commentDTO);

}
