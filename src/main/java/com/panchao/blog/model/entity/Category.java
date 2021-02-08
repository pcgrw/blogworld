package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 */
@Data
@Entity
@Table(name = "bw_category")
public class Category extends BaseEntity {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
     * 分类名称
     */
    private String categoryName;
    /*
     * 分类缩略名
     */
    private String slug;
    /*
     * 文章
     */
    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();
}
