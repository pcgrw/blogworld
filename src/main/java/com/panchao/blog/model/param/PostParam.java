package com.panchao.blog.model.param;

import lombok.Data;

import java.util.Set;

/**
 * Post Param
 */
@Data
public class PostParam {
    private String title;
    private String slug;
    private String author;
    private String excerpt;
    private String content;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean openComment;
    private boolean published;
    private boolean recommend;
    private String firstPicture;
    private String flag;
    private Long categoryId;
    private Set<Long> tagIds;
}
