package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    public List<CommentDTO> findList() {
        log.info("2) 모든 코멘트를 조회");
        List<CommentDTO>  list  = commentRepository.findAll().stream().map(i->mapper.map(i, CommentDTO.class)).toList();
        log.info("3) 모든 코멘트를 조회.{}" ,list);

        return list;
    }

    @Override
    public CommentDTO findByCommentCommentId(Long commentId) {
        log.info("해당 아이디의 코멘트를 조회 service->{}",commentId);
        return mapper.map( commentRepository.findById(commentId), CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        log.info("해당 게시글 아이디의 코멘트를을  조회 -> service :{} " ,postId);
        return commentRepository.findCommentsByPostId(postId).stream().map(i->mapper.map(i, CommentDTO.class)).toList();
    }
}