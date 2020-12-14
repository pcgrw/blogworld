package com.panchao.blog.repository;

import com.panchao.blog.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Post Repository
 */
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Modifying
    @Query("update Post post set post.views = post.views + 1 where post.id = ?1")
    int updateViews(Long id);
}
