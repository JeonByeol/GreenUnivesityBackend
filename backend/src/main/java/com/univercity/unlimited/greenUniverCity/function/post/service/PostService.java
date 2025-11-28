package com.univercity.unlimited.greenUniverCity.function.post.service;

import com.univercity.unlimited.greenUniverCity.function.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.post.repository.PostRepository;

import java.util.List;
import java.util.Optional;

public interface PostService {
    //P-2) P에 선언된 postmanTestPost의 요청을 받아서 Post에 존재하는 전체 Post
    List<PostDTO> findAllPost();

    PostDTO postById (Long postId);

    List<Post> searchById(Long postId);

    //P-3) P를 올린 아이디 추적
    //    PostResponseDTO postById (Long postId,String content);
    //p-4) 검색한 단어의 P를 추적

    List<Post> searchByKeyword(String keyword);

    //P-6) POST 작성
    PostResponseDTO postCreate (Long postId,String studentEmail );

    PostResponseDTO postCreate(PostCreateDTO dto);

    //P-7) POST 수정
    PostResponseDTO postUpdate(Long PostId, PostUpdateDTO dto);

    //P-8) POST 삭제
    void deleteByPost(Long postId);

    List<Post> search(String keyword);


//    Optional<Post> findByIdPost(Long postId);
//
//    Post getPostbyId(Long id);
}
