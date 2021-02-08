package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章
 */
@Data
@Entity
@Table(name = "bw_post")
public class Post extends BaseEntity {
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
     * 赞赏开启
     */
    @Column(name = "is_appreciation")
    private boolean appreciation;
    /*
     * 版权开启
     */
    @Column(name = "is_share_statement")
    private boolean shareStatement;
    /*
     * 评论开启
     */
    @Column(name = "is_open_comment")
    private boolean openComment;
    /*
     * 是否发布
     */
    @Column(name = "is_published")
    private boolean published;
    /*
     * 是否推荐
     */
    @Column(name = "is_recommend")
    private boolean recommend;
    /*
     * 首图
     */
    @Column(name = "first_picture")
    private String firstPicture;
    /*
     * 标记
     */
    private String flag;
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

    @Transient
    private String tagIds;

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }
}
