package com.panchao.blog.repository;

import com.panchao.blog.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Tag Repository
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTagName(String tagName);
}
