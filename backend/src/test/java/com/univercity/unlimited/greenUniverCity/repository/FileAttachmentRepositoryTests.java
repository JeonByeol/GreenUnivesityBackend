package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.entity.FileAttachment;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.repository.FileRepository;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

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
    @Transactional
    @Commit
    public void createDummyFileAttachments() {

        // 1. Post 데이터가 있는지 확인
        List<Post> posts = postRepository.findAll();

        Post post;
        if (posts.isEmpty()) {
            log.info("Post 데이터가 없습니다. 테스트용 Post를 새로 생성합니다.");

            // title, content 정도는 거의 필수일 가능성이 큼.
            post = Post.builder()
                    .title("파일 첨부 테스트용 게시글")
                    .content("FileAttachment 레포지토리 테스트용 더미 게시글입니다.")
                    // TODO: Post 엔티티에 nullable=false 필드가 더 있다면 여기에 추가로 설정해야 함.
                    // 예: .writer(user)  / .board(board) 등
                    .build();

            post = postRepository.save(post);
        } else {
            // 기존 게시글이 있으면 첫 번째 게시글 사용
            post = posts.get(0);
        }

        // 2. 선택된 Post에 파일 10개 붙이기
        for (int i = 1; i <= 10; i++) {

            FileAttachment fileAttachment = FileAttachment.builder()
                    .post(post)
                    .originalName("dummy-file-" + i + ".png")
                    .storedName("dummy-" + i + ".png")
                    .storedPath("/uploads/2025/12/dummy-" + i + ".png")
                    .size((long) (1000 * i))
                    .contentType("image/png")
                    .build();

            FileAttachment saved = fileRepository.save(fileAttachment);

            // 저장 검증
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getPost()).isNotNull();

            log.info("저장된 더미 파일 {}개차: {}", i, saved);
        }
    }


    @Test
    public void readAllFiles() {
        List<FileAttachment> list = fileRepository.findAll();

        assertThat(list).isNotEmpty();
        log.info("전체 파일 첨부 개수: {}", list.size());

        list.forEach(f -> log.info("파일 첨부: {}", f));
    }
}
