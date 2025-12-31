package com.univercity.unlimited.greenUniverCity.function.community.post.entity;

import com.univercity.unlimited.greenUniverCity.function.community.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_post")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE tbl_post SET deleted = true WHERE post_id = ?")
@Where(clause = "deleted = false")

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

    @Column(name = "update_at")
    private LocalDateTime updateAt; //개시 날짜

    @Column(name = "view_count")
    private int viewCount; //뷰 수


    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
//    @ToString.Exclude
    private Board board; //보드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @ToString.Exclude
    private User user; //유저



//    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
//    @Builder.Default
//    @ToString.Exclude
//    private List<Comment> comments = new ArrayList<>();
//
//    public void addComment(Comment comment){
//        comments.add(comment);
//    }

}
