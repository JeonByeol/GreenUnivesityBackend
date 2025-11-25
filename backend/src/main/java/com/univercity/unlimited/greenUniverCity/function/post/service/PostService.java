package com.univercity.unlimited.greenUniverCity.function.post.service;

import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDTO> findAllPost();

    Optional<Post> findByIdPost(Long postId);
//    모든 게시글 조회
//    Optional<Post> findByIdPost(Long postId);
////    아이디로 게시글 조회
//    ResponseEntity<String> addPost(PostDTO postDTO);
}
