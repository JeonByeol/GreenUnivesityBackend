package com.univercity.unlimited.greenUniverCity.function.community.comment.repository;

import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Modifying
    @Query("UPDATE Comment c SET c.deleted = true WHERE c.commentId = :id")
    void softDelete(@Param("id") Long id);

    // 관리자용: 삭제 포함 조회
    @Query("SELECT c FROM Comment c")
    List<Comment> findAllIncludingDeleted();

    // 특정 게시글 댓글 조회 (자동으로 deleted=false만)
    List<Comment> findByPost_PostId(Long postId);


}