package com.panchao.blog.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Post Query
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostQuery {
    private String title;
    private Long categoryId;
    private boolean recommend;
}
