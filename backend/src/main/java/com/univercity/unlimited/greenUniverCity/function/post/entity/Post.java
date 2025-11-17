package com.univercity.unlimited.greenUniverCity.function.post.entity;

import com.univercity.unlimited.greenUniverCity.function.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.comment.entity.Comment;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_post")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "post_id")
    private Long postId; //게시글 아이디

    @Column(name = "title")
    private String title; //타이틀

    @Column(name = "content")
    private String content; //콘텐츠

    @Column(name = "created_at")
    private LocalDateTime createAt; //개시 날짜

    @Column(name = "view_count")
    private int viewCount; //뷰 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @ToString.Exclude
    private Board board; //보드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user; //유저

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment){
        comments.add(comment);
    }

}
