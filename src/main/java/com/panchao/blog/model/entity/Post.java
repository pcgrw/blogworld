package com.panchao.blog.model.entity;

import com.panchao.blog.util.DateUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章
 */
@Data
@Entity
@Table(name = "bw_post")
public class Post {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
     * 标题
     */
    private String title;
    /*
     * 缩略名
     */
    private String slug;
    /*
     * 作者
     */
    private String author;
    /*
     * 摘录
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String excerpt;
    /*
     * 正文
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    /*
     * 是否发布
     */
    @Column(name = "is_published")
    private boolean published;
    /*
     * 首图
     */
    @Column(name = "first_picture")
    private String firstPicture;
    /*
     * 浏览次数
     */
    private Long views;
    /*
     * 分类
     */
    @ManyToOne
    private Category category;
    /*
     * 标签
     */
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();
    /*
     * 用户
     */
    @ManyToOne
    private User user;
    /*
     * 评论
     */
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
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
