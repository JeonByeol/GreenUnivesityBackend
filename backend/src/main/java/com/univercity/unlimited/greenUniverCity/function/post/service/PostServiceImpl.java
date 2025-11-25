//package com.univercity.unlimited.greenUniverCity.function.post.service;
//
//import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
//import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
//import com.univercity.unlimited.greenUniverCity.function.post.repository.PostRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//
//public class PostServiceImpl implements PostService {
//    private final PostRepository postRepository;
//    private final ModelMapper mapper;
//
//
//    // P-1) Post ServiceImpl 설정
//
//    public List<PostDTO> findAllPost() {
//        List<PostDTO> dto=new ArrayList<>();
//        for(Post i:postRepository.findAll()){
//            PostDTO r=mapper.map(i, PostDTO.class);
//            dto.add(r);
//        }
//        log.info("모든 Post를 조회하는 service 코드 실행");
//        return dto;
//    }
//    @Override
//    public Optional<Post> findByIdPost(Long postId) {
//
//        return postRepository.findById(postId);
////        log.info("한명의 회원을 조회하는 service 생성");
//    }
//
//
//}
