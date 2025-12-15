package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.entity;

import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_fileAttachment")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // FK 컬럼명
    private Post post;

    @Column(name = "stored_name", nullable = false) // 서버에 저장된 파일명
    private String storedName;

    @Column(name = "original_name", nullable = false) // 원본 파일명
    private String originalName;

    @Column(name = "stored_path", nullable = false) // 경로 또는 URL
    private String storedPath;

    @Column(name = "size")
    private Long size; // 바이트 단위 파일 크기

    @Column(name = "content_type")
    private String contentType; // image/png, application/pdf 등
}
