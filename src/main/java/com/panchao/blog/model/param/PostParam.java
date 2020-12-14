package com.panchao.blog.model.param;

import lombok.Data;

import java.util.Set;

/**
 * Post Param
 */
@Data
public class PostParam {
    private String title;
    private Long categoryId;
    private boolean recommend;
    private Set<Long> tagIds;
}
