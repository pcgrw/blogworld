package com.panchao.blog.repository;

import com.panchao.blog.model.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment Repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentCommentNull(Long postId, Sort sort);
}
