package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
@Data
@Entity
@Table(name = "bw_user")
public class User extends BaseEntity {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
     * 账户名称
     */
    private String username;
    /*
     * 昵称
     */
    private String nickname;
    /*
     * 密码
     */
    private String password;
    /*
     * 邮箱
     */
    private String email;
    /*
     * 头像
     */
    private String avatar;
    /*
     * 文章
     */
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();
}
