package com.panchao.blog.service;

import com.panchao.blog.BlogWorldApplicationTests;
import com.panchao.blog.model.entity.Category;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class CategoryServiceTest extends BlogWorldApplicationTests {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void top() {
        List<Category> top = categoryService.top(5);
        Assert.assertTrue(true);
    }

    @Test
    public void save() {
        Category category = new Category();
        category.setCategoryName("Java学习");
        category.setSlug("java");
        category = categoryService.save(category);
        Assert.assertNotNull("id不能为空", category.getId());
    }

    @Test
    public void update() {
        Category category = new Category();
        category.setSlug("JAVA");
        category = categoryService.update(2L, category);
        Assert.assertEquals("JAVA", category.getSlug());
    }

    @Test
    public void delete() {
        categoryService.delete(2L);
        Assert.assertTrue(true);
    }

    @Test
    public void page() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createTime");
        Page<Category> page = categoryService.page(pageable);
        Assert.assertEquals(1,page.getSize());
    }
}
