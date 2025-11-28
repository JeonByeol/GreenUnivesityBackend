package com.univercity.unlimited.greenUniverCity.function.comment.service;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.*;
import com.univercity.unlimited.greenUniverCity.function.comment.entity.Comment;
import com.univercity.unlimited.greenUniverCity.function.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;


    @Override
    public List<CommentResponseDTO> findAllComment() {
        return List.of();
    }

    @Override
    public List<CommentResponseDTO> findList() {
        return List.of();
    }

    @Override
    public CommentDTO findByCommentCommentId(Long commentId) {
        log.info("해당 아이디의 코멘트를 조회 service->{}",commentId);
        return mapper.map( commentRepository.findById(commentId), CommentDTO.class);
    }

    @Override
    public List<LegacyCommentDTO> getCommentsByPostId(Long postId) {
        log.info("해당 게시글 아이디의 코멘트를을  조회 -> service :{} " ,postId);
        return commentRepository.findCommentsByPostId(postId).stream().map(i->mapper.map(i, LegacyCommentDTO.class)).toList();
    }

    @Override
    public ResponseEntity<String> addComment(LegacyCommentDTO legacyCommentDTO) {
        return null;
    }

    @Override
    public Optional<List<LegacyCommentDTO>> findAllCommentDTO() {
        List<Comment> comments = commentRepository.findAll();
        List<LegacyCommentDTO> legacyCommentDTOS = comments.stream().map(comment ->
                mapper.map(comment, LegacyCommentDTO.class)).toList();

        Optional<List<LegacyCommentDTO>> optionalCommentDTOS = Optional.of(legacyCommentDTOS);
        return optionalCommentDTOS;
    }
    @Override
    public Optional<Comment> commentId(Long commentId){
        return null;
    }

    @Override
    public CommentResponseDTO commentUpdate(Long commentId, CommentUpdateDTO dto) {

        // 1) 댓글 존재 여부 확인
        Comment entity = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        // 2) 값 변경
        entity.setContent(dto.getContent());

        // 3) 저장
        Comment saved = commentRepository.save(entity);

        // 4) ResponseDTO 수동 매핑
        return CommentResponseDTO.builder()
                .commentId(saved.getCommentId())
                .content(saved.getContent())
                .build();
    }

    @Override
    public void deleteComment(Long commentId) {

    }

    @Override
    public List<CommentResponseDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(c -> CommentResponseDTO.builder()
                        .commentId(c.getCommentId())
                        .content(c.getContent())
                        .build())
                .toList();
    }

    @Override
    public CommentResponseDTO create(CommentCreateDTO dto) {
        return null;
    }

    @Override
    @Transactional
    public CommentResponseDTO updateComment(CommentUpdateDTO dto) {

        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        comment.setContent(dto.getContent());

        Comment saved = commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .commentId(saved.getCommentId())
                .content(saved.getContent())
                .build();
    }


}