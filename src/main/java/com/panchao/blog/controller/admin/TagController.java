package com.panchao.blog.controller.admin;

import com.panchao.blog.model.entity.Tag;
import com.panchao.blog.model.param.TagParam;
import com.panchao.blog.service.TagService;
import com.panchao.blog.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Tag Controller
 */
@Controller
@RequestMapping("/admin/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public String page(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                           Pageable pageable, Model model) {
        Page<Tag> page = tagService.page(pageable);
        model.addAttribute("page", page);
        return "admin/tags";
    }

    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag-input";
    }

    @PostMapping
    public String create(@RequestBody @Valid TagParam tagParam, BindingResult result,
                         RedirectAttributes attributes) {
        Tag tag = new Tag();
        BeanUtils.updateProperties(tagParam, tag);
        Tag tagTemp = tagService.getByTagName(tag.getTagName());
        if (!Objects.isNull(tagTemp)) {
            result.rejectValue("tagName", "tagNameError", "标签名称已存在");
        }
        if (result.hasErrors()) {
            return "admin/tag-input";
        }
        Tag tagResult = tagService.save(tag);
        if (Objects.isNull(tagResult)) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/{id}/input")
    public String input(@PathVariable Long id, Model model) {
        Tag tag = tagService.get(id);
        model.addAttribute("tag", tag);
        return "admin/tag-input";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody @Valid TagParam tagParam, BindingResult result,
                         RedirectAttributes attributes) {
        Tag tag = new Tag();
        BeanUtils.updateProperties(tagParam, tag);
        Tag tagTemp = tagService.getByTagName(tag.getTagName());
        if (!Objects.isNull(tagTemp)) {
            result.rejectValue("tagName", "tagNameError", "标签名称已存在");
        }
        if (result.hasErrors()) {
            return "admin/tag-input";
        }
        Tag tagResult = tagService.update(id, tag);
        if (Objects.isNull(tagResult)) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.delete(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
