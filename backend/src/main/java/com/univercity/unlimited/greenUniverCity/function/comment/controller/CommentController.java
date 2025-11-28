package com.univercity.unlimited.greenUniverCity.function.comment.controller;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.*;
import com.univercity.unlimited.greenUniverCity.function.comment.service.CommentService;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
    @RequestMapping("api/comment")
public class CommentController {
    private final CommentService commentService;


// C-1) 댓글 전체 조회 200
@GetMapping("/all")
public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
    List<CommentResponseDTO> result = commentService.findAll();
    return ResponseEntity.ok(result);
}
    // C-2) 아이디로 한 개의 댓글만 조회 200
    @GetMapping("/one/{commentId}")
    public List<CommentDTO> getById(@PathVariable("commentId") Long commentId){
        return List.of(commentService.findByCommentCommentId(commentId));
    };
    // C-5) 댓글 수정 200
    @PutMapping("/update")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @RequestBody CommentUpdateDTO dto) {
        CommentResponseDTO updated = commentService.updateComment(dto);
        return ResponseEntity.ok(updated);
    }
    // C-6) 댓글 삭제 204
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);  // ✔ 정식 메서드 호출
        return ResponseEntity.noContent().build();
    }
    // C-7) 댓글 작성 201
    @PostMapping("/create")
    public ResponseEntity<CommentResponseDTO> createComment(
            @RequestBody CommentCreateDTO dto
    ) {
        CommentResponseDTO created = commentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
