package com.panchao.blog.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Category Param
 */
@Data
public class CategoryParam {
    /**
     * 分类Id
     */
    private Long id;
    /*
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 255, message = "分类名称的字符长度不能超过 {max}")
    private String categoryName;
    /*
     * 分类缩略名
     */
    @NotBlank(message = "分类缩略名不能为空")
    @Size(max = 255, message = "分类缩略名的字符长度不能超过 {max}")
    private String slug;
}
