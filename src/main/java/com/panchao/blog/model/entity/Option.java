package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bw_option")
public class Option extends BaseEntity {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
     * 选项配置名称
     */
    private String optionName;
    /*
     * 选项配置值
     */
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String optionValue;
    /*
     * 是否有效
     */
    @Column(name = "is_valid")
    private Boolean valid = true;
}
