package com.panchao.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panchao.blog.model.entity.Comment;
import com.panchao.blog.model.entity.Post;
import com.panchao.blog.model.entity.User;
import com.panchao.blog.service.CommentService;
import com.panchao.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    @GetMapping("/{postId}")
    public String comments(@PathVariable Long postId, Model model) {
        model.addAttribute("comments", commentService.listByPostId(postId));
        return "post :: commentList";
    }

    @PostMapping
    public String create(@Valid Comment comment, HttpSession session) {
        Long postId = comment.getPost().getId();
        comment.setPost(postService.get(postId));
        User user = (User) session.getAttribute("user");
        if (!Objects.isNull(user) && !Objects.isNull(user.getAvatar())){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2344451607,2404623174&fm=11&gp=0.jpg");
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+postId;
    }
}
