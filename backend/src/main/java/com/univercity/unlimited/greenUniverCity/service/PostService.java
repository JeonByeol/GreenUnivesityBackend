package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> findAllPost();
//    모든 게시글 조회
    Optional<Post> findByIdPost(Long postId);
//    아이디로 게시글 조회

}
