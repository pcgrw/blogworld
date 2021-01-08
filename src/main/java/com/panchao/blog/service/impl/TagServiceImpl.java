package com.panchao.blog.service.impl;

import com.panchao.blog.exception.NotFoundException;
import com.panchao.blog.model.entity.Tag;
import com.panchao.blog.repository.TagRepository;
import com.panchao.blog.service.TagService;
import com.panchao.blog.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Tag Service Impl
 */
@Service

public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listAll(Sort sort) {
        return tagRepository.findAll(sort);
    }

    @Override
    public List<Tag> listAll() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listAllById(Collection<Long> ids) {
        return tagRepository.findAllById(ids);
    }

    @Transactional
    @Override
    public Tag update(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (Objects.isNull(t)) {
            throw new NotFoundException("该标签不存在");
        }
        BeanUtils.updateProperties(tag, t);
        return tagRepository.save(t);
    }

    @Override
    public List<Tag> top(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "posts.size");
        return tagRepository.findTop(pageable);
    }

    @Override
    public Page<Tag> page(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag getByTagName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }

    @Override
    public Tag get(Long id) {
        return tagRepository.getOne(id);
    }
}
