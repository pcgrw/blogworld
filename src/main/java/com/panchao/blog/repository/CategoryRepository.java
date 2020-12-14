package com.panchao.blog.repository;

import com.panchao.blog.model.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Category Repository
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select category from Category category")
    List<Category> findTop(Pageable pageable);

    Category findByCategoryName(String categoryName);
}
