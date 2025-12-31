package com.univercity.unlimited.greenUniverCity.function.community.post.controller;

import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.service.CommentService;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /* =========================
       공통 유틸 (DepartmentController와 동일)
    ========================= */
    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "test@aaa.com";
        }

        return email;
    }

    /* =========================
       P-1) 게시물 전체 조회
    ========================= */
    @GetMapping("/all")
    public List<PostDTO> getPostList() {
        log.info("1) 게시물 전체 조회 Controller");
        return postService.findAllPost();
    }

    /* =========================
       P-2) 게시물 단건 조회
    ========================= */
    @GetMapping("/one/{postId}")
    public PostResponseDTO getPostById(
            @PathVariable("postId") Long postId
    ) {
        log.info("1) 게시물 단건 조회 postId={}", postId);
        return postService.postById(postId);
    }

    /* =========================
       P-3) 게시물 생성
    ========================= */
    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostCreateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    ) {
        log.info("1) 게시물 생성 요청 {}", dto);

        String email = getEmail(requesterEmail);
        PostResponseDTO created = postService.postCreate(dto, email);

        return ResponseEntity.ok(created);
    }

    /* =========================
       P-4) 게시물 수정
       (DepartmentController update와 동일: Body 기반)
    ========================= */
    @PutMapping("/update")
    public ResponseEntity<PostResponseDTO> updatePost(
            @RequestBody PostUpdateDTO dto,
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    ) {
        log.info("1) 게시물 수정 요청 {}", dto);

        String email = getEmail(requesterEmail);
        PostResponseDTO updated = postService.postUpdate(dto.getPostId(),email);

        return ResponseEntity.ok(updated);
    }

    /* =========================
       P-5) 게시물 삭제
    ========================= */
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable("postId") Long postId,
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    ) {
        log.info("1) 게시물 삭제 요청 postId={}", postId);

        postService.deletePost(postId);

        return ResponseEntity.ok("게시물 삭제 완료");
    }

    @GetMapping("/{postId}/comment")
    public ResponseEntity<List<CommentResponseDTO>> getCommentByPostId(
            @PathVariable Long postId,
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    ) {
        List<CommentResponseDTO> comments = commentService.findByPostId(postId);

        log.info("게시글 기반 postId={}, 댓글 수={}", postId, comments.size());

        return ResponseEntity.ok(comments);
    }

}
