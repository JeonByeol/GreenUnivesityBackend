package com.univercity.unlimited.greenUniverCity.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "tbl_test")
@ToString
@Builder
public class ReTestVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;


}
