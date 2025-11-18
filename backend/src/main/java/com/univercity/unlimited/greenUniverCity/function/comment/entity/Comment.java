package com.univercity.unlimited.greenUniverCity.function.comment.entity;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString
@Table(name="tbl_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column (name = "content")
    private String content;

    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//  @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
//  @ToString.Exclude
    private Post post;
}
