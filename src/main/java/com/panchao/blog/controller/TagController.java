package com.panchao.blog.controller;

import com.panchao.blog.model.entity.Post;
import com.panchao.blog.model.entity.Tag;
import com.panchao.blog.service.PostService;
import com.panchao.blog.service.TagService;
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
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private PostService postService;

    @GetMapping
    public String tags(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        List<Tag> tags = tagService.top(100);
        Page<Post> posts = postService.page(pageable);
        model.addAttribute("tags", tags);
        model.addAttribute("page", posts);
        model.addAttribute("activeTagId", 0);
        return "tags";
    }

    @GetMapping("/{id}")
    public String tags(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model) {
        List<Tag> tags = tagService.top(100);
        if (id <= 0) {
            id = tags.get(0).getId();
        }
        Page<Post> posts = postService.pageByTagId(id, pageable);
        model.addAttribute("tags", tags);
        model.addAttribute("page", posts);
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
