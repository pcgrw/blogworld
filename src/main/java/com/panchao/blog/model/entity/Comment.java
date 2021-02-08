package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 */
@Data
@Entity
@Table(name = "bw_comment")
public class Comment extends BaseEntity {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
     * 评论者
     */
    private String author;
    /*
     * 评论者头像
     */
    private String avatar;
    /*
     * 评论者邮箱
     */
    private String email;
    /*
     * 评论者网址
     */
    private String url;
    /*
     * 评论内容
     */
    private String content;
    /*
     * 文章
     */
    @ManyToOne
    private Post post;
    /*
     * 上级评论
     */
    @ManyToOne
    private Comment parentComment;
    /*
     * 下级评论
     */
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComment = new ArrayList<>();
    /*
     * 是否管理员评论
     */
    private boolean adminComment;
}
