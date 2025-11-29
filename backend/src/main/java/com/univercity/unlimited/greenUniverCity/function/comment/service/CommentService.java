package com.univercity.unlimited.greenUniverCity.function.comment.service;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.*;
import com.univercity.unlimited.greenUniverCity.function.comment.entity.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    //모든 게시글 목록
    List<CommentResponseDTO> findAllComment();

    List<CommentResponseDTO> findList();

    // 게시글별 댓글 목록
    CommentResponseDTO findByCommentCommentId(Long commentId);

    List<LegacyCommentDTO> getCommentsByPostId(Long postId);

    ResponseEntity<String> addComment(LegacyCommentDTO legacyCommentDTO);

    Optional<List<LegacyCommentDTO>> findAllCommentDTO();

    Optional<Comment> commentId(Long commentId);

    CommentResponseDTO commentUpdate(Long commentId, CommentUpdateDTO dto);

    void deleteComment(Long commentId);

    List<CommentResponseDTO> findAll();



    CommentResponseDTO createComment(CommentCreateDTO dto, String email);

    CommentResponseDTO updateComment(CommentUpdateDTO dto);


}
