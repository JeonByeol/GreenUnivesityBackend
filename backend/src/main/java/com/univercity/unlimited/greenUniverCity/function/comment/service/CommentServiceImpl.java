package com.univercity.unlimited.greenUniverCity.function.comment.service;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.LegacyCommentDTO;
import com.univercity.unlimited.greenUniverCity.function.comment.entity.Comment;
import com.univercity.unlimited.greenUniverCity.function.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;


    @Override
    public List<LegacyCommentDTO> findList() {
//        log.info("2) 모든 코멘트를 조회");
//        List<CommentDTO>  list  = commentRepository.findAll().stream().map(i->mapper.map(i, CommentDTO.class)).toList();
//        log.info("3) 모든 코멘트를 조회.{}" ,list);
//
//        return list;
        List<LegacyCommentDTO> dto=new ArrayList<>();
        for(Comment i:commentRepository.findAll()){
            LegacyCommentDTO r=mapper.map(i, LegacyCommentDTO.class);
            dto.add(r);
        }
        return dto;
    }

    @Override
    public LegacyCommentDTO findByCommentCommentId(Long commentId) {
        log.info("해당 아이디의 코멘트를 조회 service->{}",commentId);
        return mapper.map( commentRepository.findById(commentId), LegacyCommentDTO.class);
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
}