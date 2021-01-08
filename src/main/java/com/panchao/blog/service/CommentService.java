package com.panchao.blog.service;

import com.panchao.blog.model.entity.Comment;

import java.util.List;

/**
 * Comment Service
 */
public interface CommentService {
    Comment saveComment(Comment comment);

    List<Comment> listByPostId(Long postId);
}
