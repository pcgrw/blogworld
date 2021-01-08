package com.panchao.blog.repository;

import com.panchao.blog.model.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Tag Repository
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTagName(String tagName);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
