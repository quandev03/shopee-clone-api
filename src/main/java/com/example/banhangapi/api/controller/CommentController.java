package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.request.RequestAddComment;
import com.example.banhangapi.api.service.CommentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentController {
    @Autowired
    CommentService commentService;

    @RequestMapping(value = "add-comment", method = RequestMethod.POST)
    public ResponseEntity<?> addComment(@RequestBody RequestAddComment comment, @RequestParam Long idProduce) {
        return this.commentService.addComment(comment, idProduce);
    }
    @RequestMapping(value = "edit-comment", method = RequestMethod.PUT)
    public ResponseEntity<?> editComment(@RequestBody RequestAddComment comment, @RequestParam Long idComment) {
        return this.commentService.editComment(comment, idComment);
    }

    @RequestMapping(value = "delete-comment", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam Long idComment) {
        return this.commentService.deleteAComment(idComment);
    }
}
