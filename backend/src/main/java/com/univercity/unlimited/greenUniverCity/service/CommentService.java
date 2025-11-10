package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.BoardDTO;
import com.univercity.unlimited.greenUniverCity.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Post;
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
