package com.univercity.unlimited.greenUniverCity.function.community.comment.service;

import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.LegacyCommentDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    //모든 게시글 목록
    List<CommentResponseDTO> findAll();
    List<CommentResponseDTO> findAllComment();

    // 게시글별 댓글 목록
    CommentResponseDTO findByCommentCommentId(Long commentId);

    List<LegacyCommentDTO> getCommentsByPostId(Long postId);

    ResponseEntity<String> addComment(LegacyCommentDTO legacyCommentDTO);

    Optional<List<LegacyCommentDTO>> findAllCommentDTO();

    Optional<Comment> commentId(Long commentId);

    List<CommentResponseDTO> findList();

    CommentResponseDTO createComment(CommentCreateDTO dto, String email);

    CommentResponseDTO updateComment(CommentUpdateDTO dto);
    CommentResponseDTO commentUpdate(Long commentId, CommentUpdateDTO dto);

    void deleteComment(Long commentId);








}
