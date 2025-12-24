package com.univercity.unlimited.greenUniverCity.function.community.comment.service;

import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.dto.CommentUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.comment.entity.Comment;
import com.univercity.unlimited.greenUniverCity.function.community.comment.repository.CommentRepository;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;


    //조회
    @Override
    public CommentResponseDTO findByCommentCommentId(Long commentId) {
        log.info("해당 아이디의 코멘트를 조회 service->{}",commentId);
        return mapper.map( commentRepository.findById(commentId), CommentResponseDTO.class);
    }


    @Override
    public List<CommentResponseDTO> findAll() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentResponseDTO> legacyCommentDTOS = comments.stream().map(comment ->
                mapper.map(comment, CommentResponseDTO.class)).toList();

       List<CommentResponseDTO> optionalCommentDTOS = legacyCommentDTOS;
        return optionalCommentDTOS;
    }

    //생성
    @Override
    public CommentResponseDTO createComment(CommentCreateDTO dto, String email) {
        log.info("2)comment 추가 시작 comment : {}", dto);

        Comment comment = new Comment();
        MapperUtil.updateFrom(dto, comment, List.of("commentId"));
        log.info("3)CourseCreateDTO -> comment : {}", comment);

        Comment result = commentRepository.save(comment);
        log.info("4)추가된 comment : {}", result);

        return CommentResponseDTO.builder().build();
    }
    //추가
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
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //삭제
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}