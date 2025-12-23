package com.univercity.unlimited.greenUniverCity.function.community.comment.service;

import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    //모든 게시글 목록
    // C-1 전체 조회
    List<CommentResponseDTO> findAll();
    // C-2 생성
    CommentResponseDTO createComment(CommentCreateDTO dto, String email);
    // C-3 추가
    CommentResponseDTO updateComment(CommentUpdateDTO dto);
    // C-4 삭제
    void deleteComment(Long commentId);
    // C-5 게시글별 댓글 목록
    CommentResponseDTO findByCommentCommentId(Long commentId);

}
