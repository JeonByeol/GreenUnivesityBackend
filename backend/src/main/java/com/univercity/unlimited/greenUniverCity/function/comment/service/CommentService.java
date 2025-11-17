package com.univercity.unlimited.greenUniverCity.function.comment.service;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.CommentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    //모든 게시글 목록
    List<CommentDTO> findList();
    // 게시글별 댓글 목록
    CommentDTO findByCommentCommentId(Long commentId);

    List<CommentDTO> getCommentsByPostId(Long postId);

    ResponseEntity<String> addComment(CommentDTO commentDTO);

    Optional<List<CommentDTO>> findAllCommentDTO();
}
