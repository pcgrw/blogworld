package com.panchao.blog.service;

import com.panchao.blog.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;

/**
 * Tag Service
 */
public interface TagService {
    Tag save(Tag tag);

    void delete(Long id);

    List<Tag> listAll(Sort sort);

    List<Tag> listAll();

    List<Tag> listAllById(Collection<Long> ids);

    Tag update(Long id, Tag tag);

    List<Tag> top(Integer size);

    Page<Tag> page(Pageable pageable);

    Tag getByTagName(String tagName);

    Tag get(Long id);
}
