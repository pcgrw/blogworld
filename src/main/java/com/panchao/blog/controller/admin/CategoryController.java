package com.panchao.blog.controller.admin;

import com.panchao.blog.model.entity.Category;
import com.panchao.blog.model.param.CategoryParam;
import com.panchao.blog.service.CategoryService;
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
 * Category Controller
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-input";
    }

    @PostMapping
    public String create(@Valid CategoryParam categoryParam, BindingResult result,
                         RedirectAttributes attributes) {
        Category category = new Category();
        BeanUtils.updateProperties(categoryParam, category);
        Category categoryTemp = categoryService.getByCategoryName(category.getCategoryName());
        if (!Objects.isNull(categoryTemp)) {
            result.rejectValue("categoryName", "categoryNameError", "分类名称已存在");
        }
        if (result.hasErrors()) {
            return "admin/category-input";
        }
        Category categoryResult = categoryService.save(category);
        if (Objects.isNull(categoryResult)) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping
    public String page(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                           Pageable pageable, Model model) {
        Page<Category> page = categoryService.page(pageable);
        model.addAttribute("page", page);
        return "/admin/categories";
    }

    @GetMapping("/{id}/input")
    public String input(@PathVariable Long id, Model model) {
        Category category = categoryService.get(id);
        model.addAttribute("category", category);
        return "admin/category-input";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody @Valid CategoryParam categoryParam, BindingResult result,
                         RedirectAttributes attributes) {
        Category category = new Category();
        BeanUtils.updateProperties(categoryParam, category);
        Category categoryTemp = categoryService.getByCategoryName(category.getCategoryName());
        if (!Objects.isNull(categoryTemp)) {
            result.rejectValue("categoryName", "categoryNameError", "分类名称已存在");
        }
        if (result.hasErrors()) {
            return "admin/category-input";
        }
        Category categoryResult = categoryService.update(id, category);
        if (Objects.isNull(categoryResult)) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        categoryService.delete(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/categories";
    }
}
