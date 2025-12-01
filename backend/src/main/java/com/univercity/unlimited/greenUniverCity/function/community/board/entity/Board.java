package com.univercity.unlimited.greenUniverCity.function.community.board.entity;

import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString
@Table(name="tbl_board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name="board_name")
    private String boardName;

//    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
//    @Builder.Default
//    @ToString.Exclude
//    private List<Post> posts = new ArrayList<>();

//    public void addPost(Post post){
//        posts.add(post);
//    }

}
