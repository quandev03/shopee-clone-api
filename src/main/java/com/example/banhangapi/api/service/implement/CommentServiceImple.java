package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.CommentDTO;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.mapper.CommentMapper;
import com.example.banhangapi.api.repository.ProductRepository;
import com.example.banhangapi.api.repository.UserRepository;
import com.example.banhangapi.api.entity.Comment;
import com.example.banhangapi.api.repository.CommentRepository;
import com.example.banhangapi.api.request.RequestAddComment;
import com.example.banhangapi.api.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImple implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CommentMapper commentMapper;

    public ResponseEntity<?> addComment(RequestAddComment comment, String idProduct) {
        try {
            if(!productRepository.existsById(idProduct)) {
                return new ResponseEntity<>("Product dont exist",HttpStatus.NOT_FOUND);
            }
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Not found"));
            ProductEntity product = this.productRepository.findById(idProduct).get();
            Comment newComment = new Comment().builder()
                    .content(comment.getContent())
                    .createBy(user)
                    .product(product)
                    .build();
            commentRepository.save(newComment);
            return new ResponseEntity<>("Comment successfully added", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving authentication: " + e.getMessage());
        }
    }

    public ResponseEntity<?> editComment(RequestAddComment contentEdit, String idComment) {
        try {
            Comment comment = commentRepository.findById(idComment).orElseThrow(() -> new RuntimeException("Comment not found"));
                if(comment.getCreateBy().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                    comment.setContent(contentEdit.getContent());
                    commentRepository.save(comment);
                    return new ResponseEntity<>("Comment successfully edited", HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("You are not allowed to edit this comment",HttpStatus.FORBIDDEN);
                }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteAComment(String idComment) {
        try {
            Comment comment = commentRepository.findById(idComment).orElseThrow(() -> new RuntimeException("Comment not found"));
            CommentDTO commentDTO = commentMapper.toCommentDTO(comment);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if( authentication.getAuthorities().toArray()[0].toString().equals("ROLE_ADMIN") ||SecurityContextHolder.getContext().getAuthentication().getName().equals(commentDTO.getCreateBy().getUsername())) {
                commentRepository.delete(comment);
                return new ResponseEntity<>("Comment edited successfully",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("You are not allowed to delete this comment",HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Comment not found", HttpStatus.NOT_FOUND);
        }
    }

    public  ResponseEntity<?> getCommentListCommentOfProduct(Long idProduct, int page, int size){
        return null;
    }
}
