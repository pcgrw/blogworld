package com.panchao.blog.controller.admin;

import com.panchao.blog.model.entity.Category;
import com.panchao.blog.model.entity.Post;
import com.panchao.blog.model.entity.User;
import com.panchao.blog.model.param.PostParam;
import com.panchao.blog.model.param.PostQuery;
import com.panchao.blog.service.CategoryService;
import com.panchao.blog.service.PostService;
import com.panchao.blog.service.TagService;
import com.panchao.blog.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Post Controller
 */
@Controller
@RequestMapping("/admin/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @GetMapping
    public String page(
        @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
        PostQuery postQuery,
        Model model) {
        List<Category> categories = categoryService.listAll();
        model.addAttribute("categories", categories);
        model.addAttribute("page", postService.page(postQuery, pageable));
        return "/admin/posts";
    }

    @GetMapping("/search")
    public String search(
        @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
        PostQuery postQuery,
        Model model) {
        model.addAttribute("page", postService.page(postQuery, pageable));
        return "/admin/posts :: postList";
    }

    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("tags", tagService.listAll());
        model.addAttribute("post", new Post());
        return "admin/post-input";
    }

    @GetMapping("/{id}/input")
    public String input(@PathVariable Long id, Model model) {
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("tags", tagService.listAll());
        Post post = postService.get(id);
        model.addAttribute("post", post);
        return "admin/post-input";
    }

    @PostMapping
    public String create(@RequestBody @Valid PostParam postParam, RedirectAttributes attributes,
                         HttpSession session) {
        Post post = new Post();
        BeanUtils.updateProperties(postParam, post);
        post.setUser((User) session.getAttribute("user"));
        post.setCategory(categoryService.get(postParam.getCategoryId()));
        post.setTags(tagService.listAllById(postParam.getTagIds()));
        //新增
        Post p = postService.save(post);
        if (p == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/posts";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody @Valid PostParam postParam, RedirectAttributes attributes,
                         HttpSession session) {
        Post post = new Post();
        BeanUtils.updateProperties(postParam, post);
        post.setUser((User) session.getAttribute("user"));
        post.setCategory(categoryService.get(postParam.getCategoryId()));
        post.setTags(tagService.listAllById(postParam.getTagIds()));
        //修改
        Post p = postService.update(id, post);
        if (p == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/posts";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        postService.delete(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/posts";
    }
}
