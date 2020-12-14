package com.panchao.blog.service;

import com.panchao.blog.model.entity.Post;
import com.panchao.blog.model.param.PostQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Post Service
 */
public interface PostService {

    Page<Post> page(PostQuery postQuery, Pageable pageable);

    Page<Post> page(Pageable pageable);

    List<Post> recommendTop(Integer size);

    Post save(Post post);

    Post update(Long id, Post post);

    Post get(Long id);

    Post getAndConvert(Long id);

    void delete(Long id);
}
