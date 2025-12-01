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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Setter

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;


    // P-3) Post ServiceImpl 설정
    @Override
    public List<PostDTO> findAllPost() {
        List<PostDTO> dto=new ArrayList<>();
        for(Post i:postRepository.findAll()){
            PostDTO r=mapper.map(i, PostDTO.class);
            dto.add(r);
        }
        log.info("모든 Post를 조회하는 service 코드 실행");
        return dto;
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
        }

    @Override
    public PostDTO postById(Long postId) {
        Optional<Post> findById = postRepository.findById(postId);
        PostDTO r =mapper.map(findById.get(), PostDTO.class);
        return r;
    }

    @Override
    public List<Post> searchById(Long postId) {
        return List.of();
    }

    @Override
    public List<Post> searchByKeyword(String keyword) {
        return List.of();
    }

    @Override
    public PostResponseDTO postCreate(Long postId, String studentEmail) {
        return null;
    }

    @Override
    public PostResponseDTO postCreate(PostCreateDTO dto) {
        Post entity = mapper.map(dto, Post.class);
        entity.setCreateAt(LocalDateTime.now());
        Post saved = postRepository.save(entity);
        PostResponseDTO response = mapper.map(saved, PostResponseDTO.class);
        return response;
    }

   @Transactional
   @Override
    public PostResponseDTO postUpdate(Long postId, PostUpdateDTO dto) {

        // 1) 기존 게시글 조회 (board, user 다 살아있는 상태로 가져옴)
        Post entity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다"));
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        Post saved = postRepository.save(entity);
        return  mapper.map(saved, PostResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteByPost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<Post> search(String keyword) {
        return postRepository.findByTitleContainingIgnoreCase(keyword);
    }

//    @Override
//    public Post getPostbyId(Long id){
//        return postRepository.findById(id)
//                .orElseThrow();
////        log.info("한명의 회원을 조회하는 service 생성");
//    }


}
