package com.panchao.blog.service.impl;

import com.panchao.blog.exception.NotFoundException;
import com.panchao.blog.model.entity.Category;
import com.panchao.blog.repository.CategoryRepository;
import com.panchao.blog.service.CategoryService;
import com.panchao.blog.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Category Service Impl
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> top(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "posts.size");
        return categoryRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category update(Long id, Category category) {
        Category c = categoryRepository.getOne(id);
        if (Objects.isNull(c)) {
            throw new NotFoundException("该分类不存在");
        }
        BeanUtils.updateProperties(category, c);
        return categoryRepository.save(c);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<Category> page(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category getByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public Category get(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }
}
