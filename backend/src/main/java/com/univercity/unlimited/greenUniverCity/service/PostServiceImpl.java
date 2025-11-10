package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.entity.Post;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.PostRepository;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
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

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public List<Post> findAllPost() {
        log.info("모든 유저를 조회하는 service 코드 실행");
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findByIdPost(Long postId) {
        log.info("한명의 회원을 조회하는 service 생성");
        return postRepository.findById(postId);
    }

    @Override
    public ResponseEntity<String> addPost(PostDTO postDTO) {
        return null;
    }

}
