package com.univercity.unlimited.greenUniverCity.function.post.service;

import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> findAllPost();
//    모든 게시글 조회
//    Optional<Post> findByIdPost(Long postId);
////    아이디로 게시글 조회
//    ResponseEntity<String> addPost(PostDTO postDTO);
}
