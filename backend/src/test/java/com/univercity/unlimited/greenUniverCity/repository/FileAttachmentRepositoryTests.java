package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.entity.FileAttachment;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.repository.FileRepository;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class FileAttachmentRepositoryTests {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Tag("push")
    public void fileCreated() {

        // 1. Post 하나 가져오기
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            throw new IllegalStateException("Post 데이터가 없습니다. 최소 1개 이상 필요합니다!");
        }

        Post post = posts.get(0); // 첫 번째 게시글에 파일을 붙인다고 가정

        // 2. 테스트용 FileAttachment 엔티티 생성
        FileAttachment file = FileAttachment.builder()
                .post(post)
                .originalName("test-image.png")
                .storedName("1.png")
                .storedPath("/uploads/2025/12/2025_12_04_test-image.png")
                .size(12345L)
                .contentType("image/png")
                .build();

        // 3. 저장
        FileAttachment saved = fileRepository.save(file);

        // 4. 검증 + 로그
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPost()).isNotNull();
        log.info("저장된 파일 첨부: {}", saved);
    }
}
