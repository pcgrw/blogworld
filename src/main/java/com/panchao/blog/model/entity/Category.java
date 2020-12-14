package com.panchao.blog.model.entity;

import com.panchao.blog.util.DateUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分类
 */
@Data
@Entity
@Table(name = "bw_category")
public class Category {
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

    /*
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /*
     * 更新时间
     */
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    protected void prePersist() {
        Date now = DateUtils.now();
        if (createTime == null) {
            createTime = now;
        }

        if (updateTime == null) {
            updateTime = now;
        }
    }

    @PreUpdate
    protected void preUpdate() {
        updateTime = DateUtils.now();
    }

    @PreRemove
    protected void preRemove() {
        updateTime = DateUtils.now();
    }
}
