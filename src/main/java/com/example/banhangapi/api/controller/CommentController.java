package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.request.RequestAddComment;
import com.example.banhangapi.api.service.implement.CommentServiceImple;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentController {
    @Autowired
    CommentServiceImple commentServiceImple;

    @RequestMapping(value = "add-comment", method = RequestMethod.POST)
    public ResponseEntity<?> addComment(@RequestBody RequestAddComment comment, @RequestParam String idProduce) {
        return this.commentServiceImple.addComment(comment, idProduce);
    }
    @RequestMapping(value = "edit-comment", method = RequestMethod.PUT)
    public ResponseEntity<?> editComment(@RequestBody RequestAddComment comment, @RequestParam String idComment) {
        return this.commentServiceImple.editComment(comment, idComment);
    }

    @RequestMapping(value = "delete-comment", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam String idComment) {
        return this.commentServiceImple.deleteAComment(idComment);
    }
}
