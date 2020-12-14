package com.panchao.blog.service;

import com.panchao.blog.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Category Service
 */
public interface CategoryService {
    List<Category> top(Integer size);

    Category save(Category category);

    Category update(Long id, Category category);

    void delete(Long id);

    Page<Category> page(Pageable pageable);

    Category getByCategoryName(String categoryName);

    Category get(Long id);

    List<Category> listAll();
}
