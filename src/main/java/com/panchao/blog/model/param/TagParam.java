package com.panchao.blog.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Tag Param
 */
@Data
public class TagParam {
    /*
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 255, message = "标签名称的字符长度不能超过 {max}")
    private String tagName;
    /*
     * 标签缩略名
     */
    @NotBlank(message = "标签缩略名不能为空")
    @Size(max = 255, message = "标签缩略名的字符长度不能超过 {max}")
    private String slug;
}
