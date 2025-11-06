package com.univercity.unlimited.greenUniverCity.entity;

import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@ToString
@Table(name="tbl_Board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "board_id")
    private Long boardId;
    @Column(name="board_name")
    private String boardName;


}
