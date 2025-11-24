package com.univercity.unlimited.greenUniverCity.function.comment.service;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.LegacyCommentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    //모든 게시글 목록
    List<LegacyCommentDTO> findList();
    // 게시글별 댓글 목록
    LegacyCommentDTO findByCommentCommentId(Long commentId);

    List<LegacyCommentDTO> getCommentsByPostId(Long postId);

    ResponseEntity<String> addComment(LegacyCommentDTO legacyCommentDTO);

    Optional<List<LegacyCommentDTO>> findAllCommentDTO();
}
