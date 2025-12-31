package com.univercity.unlimited.greenUniverCity.function.community.comment.service;

import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;

import java.util.List;

public interface CommentService {

    /** C-1 전체 조회 (일반 사용자용: deleted=false) */
    List<CommentResponseDTO> findAll();

    /** C-2 댓글 생성 */
    CommentResponseDTO createComment(CommentCreateDTO dto);

    /** C-3 댓글 수정 */
    CommentResponseDTO updateComment(CommentUpdateDTO dto);

    /** C-4 댓글 삭제 (Soft Delete) */
    void deleteComment(Long commentId);

    /** C-5 단일 댓글 조회 */
    CommentResponseDTO findByCommentId(Long commentId);

    /** 관리자용: 삭제 포함 전체 조회 */
    List<Comment> getAllCommentsForAdmin();
    /** C-6 게시글 기준 댓글 조회 */
    List<CommentResponseDTO> findByPostId(Long postId);
}
