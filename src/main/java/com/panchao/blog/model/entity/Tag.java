package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 */
@Data
@Entity
@Table(name = "bw_tag")
public class Tag extends BaseEntity {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
     * 标签名称
     */
    private String tagName;
    /*
     * 标签缩略名
     */
    private String slug;
    /*
     * 文章
     */
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();
}
