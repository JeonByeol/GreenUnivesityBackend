package com.univercity.unlimited.greenUniverCity.function.community.post.service;

import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import jakarta.transaction.Transactional;

import java.util.List;

public interface PostService {
    //P-2) P에 선언된 postmanTestPost의 요청을 받아서 Post에 존재하는 전체 Post
    List<PostDTO> findAllPost();

    PostResponseDTO postById (Long postId);

   //P-6) POST 작성
    PostResponseDTO postCreate (PostCreateDTO postId,String studentEmail );;

    //P-7) POST 수정
    PostResponseDTO postUpdate(Long PostId, String email);

    // P-3) 추가
    @Transactional
    PostResponseDTO postUpdate(Long postId, PostUpdateDTO dto);

    //P-8) POST 삭제
    void deletePost(Long postId);

    //P-3) P를 올린 아이디 추적
    //    PostResponseDTO postById (Long postId,String content);
    //p-4) 검색한 단어의 P를 추적
//    List<Post> searchByKeyword(String keyword);
}
