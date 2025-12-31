package com.univercity.unlimited.greenUniverCity.function.community.post.service;

import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Setter
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;


    // P-1) 조회
    @Override
    public List<PostDTO> findAllPost() {
        List<PostDTO> dto = new ArrayList<>();
        for (Post i : postRepository.findAll()) {
            PostDTO r = mapper.map(i, PostDTO.class);
            dto.add(r);
        }
        log.info("모든 Post를 조회하는 service 코드 실행");
        return dto;
    }

    // P-1-1) 아이디로 조회
    @Override
    public PostResponseDTO postById(Long postId) {
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 존재하지 않습니다."));

        return PostResponseDTO.builder()
                .boardName(p.getBoard().getBoardName())
                .userId(p.getUser().getUserId())
                .title(p.getTitle())
                .content(p.getContent())
                .viewCount(p.getViewCount())
                .createdAt(p.getCreateAt())
                .updateAt(p.getUpdateAt())
                .build();
    }


    // P-2) 생성
    @Override
    public PostResponseDTO postCreate(PostCreateDTO postId, String studentEmail) {
        Post entity = mapper.map(postId, Post.class);
        entity.setCreateAt(LocalDateTime.now());
        Post saved = postRepository.save(entity);
        PostResponseDTO response = mapper.map(saved, PostResponseDTO.class);
        return response;
    }

    @Override
    public PostResponseDTO postUpdate(Long PostId, String email) {
        return null;
    }

    // P-3) 추가
    @Transactional
    @Override
    public PostResponseDTO postUpdate(Long postId, PostUpdateDTO dto) {

        // 1) 기존 게시글 조회 (board, user 다 살아있는 상태로 가져옴)
        Post entity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다"));
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        Post saved = postRepository.save(entity);
        return mapper.map(saved, PostResponseDTO.class);
    }

    // P-4) 삭제
    @Override
    public void deletePost(Long postId) {
        postRepository.softDelete(postId);
    }
}

//    @Override
//    public PostResponseDTO postCreate(PostCreateDTO dto) {
//
//    }
//
//    @Override
//    public List<Post> searchByKeyword(String keyword) {
//        return List.of();
//    }



