package com.panchao.blog.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bw_attachment")
public class Attachment extends BaseEntity {
    /*
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
