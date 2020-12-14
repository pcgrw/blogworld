package com.panchao.blog.model.param;

import lombok.Data;

/**
 * Post Query
 */
@Data
public class PostQuery {
    private String title;
    private Long categoryId;
    private boolean recommend;
}
