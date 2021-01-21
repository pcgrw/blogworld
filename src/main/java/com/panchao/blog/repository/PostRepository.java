package com.panchao.blog.repository;

import com.panchao.blog.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Post Repository
 */
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Transactional
    @Modifying
    @Query("update Post post set post.views = post.views + 1 where post.id = ?1")
    int updateViews(Long id);

    @Query("select post from Post post where post.title like ?1 or post.content like ?1")
    Page<Post> findByQuery(String query, Pageable pageable);
}
