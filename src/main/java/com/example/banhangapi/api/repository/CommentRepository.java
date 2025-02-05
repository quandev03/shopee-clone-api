package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
//    @EntityGraph(attributePaths = {"create_by"})
    Optional<Comment> findById(Long id);
}
