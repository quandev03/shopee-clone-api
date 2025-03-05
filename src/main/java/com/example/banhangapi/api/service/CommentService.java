package com.example.banhangapi.api.service;

import com.example.banhangapi.api.request.RequestAddComment;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    public ResponseEntity<?> addComment(RequestAddComment comment, String idProduct);
    public ResponseEntity<?> editComment(RequestAddComment contentEdit, String idComment);
    public ResponseEntity<?> deleteAComment(String idComment);
    public  ResponseEntity<?> getCommentListCommentOfProduct(Long idProduct, int page, int size);

}
