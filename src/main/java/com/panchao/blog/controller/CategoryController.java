package com.panchao.blog.controller;

import com.panchao.blog.model.entity.Category;
import com.panchao.blog.model.entity.Post;
import com.panchao.blog.model.param.PostQuery;
import com.panchao.blog.service.CategoryService;
import com.panchao.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;

    @GetMapping
    public String categories(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             Model model) {
        List<Category> categories = categoryService.top(100);
        PostQuery postQuery = PostQuery.builder().build();
        Page<Post> posts = postService.page(postQuery, pageable);
        model.addAttribute("categories", categories);
        model.addAttribute("page", posts);
        model.addAttribute("activeCategoryId", 0);
        return "categories";
    }

    @GetMapping("/{id}")
    public String categories(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable Long id, Model model) {
        List<Category> categories = categoryService.top(100);
        if (id <= 0) {
            id = categories.get(0).getId();
        }
        PostQuery postQuery = PostQuery.builder().categoryId(id).build();
        Page<Post> posts = postService.page(postQuery, pageable);
        model.addAttribute("categories", categories);
        model.addAttribute("page", posts);
        model.addAttribute("activeCategoryId", id);
        return "categories";
    }
}
