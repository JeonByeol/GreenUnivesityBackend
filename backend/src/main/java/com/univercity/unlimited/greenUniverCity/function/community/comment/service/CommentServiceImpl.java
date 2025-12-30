package com.univercity.unlimited.greenUniverCity.function.community.comment.service;

import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;
import com.univercity.unlimited.greenUniverCity.function.community.comment.repository.CommentRepository;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /** C-1 전체 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(CommentResponseDTO::from)
                .toList();
    }


    /** C-2 생성 */
    @Override
    public CommentResponseDTO createComment(CommentCreateDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        return CommentResponseDTO.from(commentRepository.save(comment));
    }

    /** C-3 수정 */
    @Override
    public CommentResponseDTO updateComment(CommentUpdateDTO dto) {
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        comment.setContent(dto.getContent());
        return CommentResponseDTO.from(comment);
    }

    /** C-4 삭제 (Soft Delete) */
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.softDelete(commentId);
    }

    /** C-5 단일 조회 */
    @Override
    @Transactional(readOnly = true)
    public CommentResponseDTO findByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        return CommentResponseDTO.from(comment);
    }

    /** 관리자용 전체 조회 (deleted 포함) */
    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentsForAdmin() {
        return commentRepository.findAllIncludingDeleted();
    }
}
