package com.panchao.blog.controller;

import com.panchao.blog.service.CategoryService;
import com.panchao.blog.service.PostService;
import com.panchao.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName IndexController
 * @Desc: TODO
 */
@Controller
public class IndexController {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(
        @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
        Model model) {
        model.addAttribute("postPage", postService.page(pageable));
        model.addAttribute("categories", categoryService.top(5));
        model.addAttribute("tags", tagService.top(5));
        model.addAttribute("recommendPosts", postService.recommendTop(5));
        return "index";
    }
}
