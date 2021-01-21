package com.panchao.blog.controller;

import com.panchao.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/archives")
public class ArchiveController {
    @Autowired
    private PostService postService;

    @GetMapping
    public String archives(Model model) {
        model.addAttribute("archiveMap", postService.archivePost());
        model.addAttribute("postCount", postService.countPost());
        return "archives";
    }
}
