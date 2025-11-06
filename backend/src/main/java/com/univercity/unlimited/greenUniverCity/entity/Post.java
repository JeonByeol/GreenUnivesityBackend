package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tbl_post")
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;
}
