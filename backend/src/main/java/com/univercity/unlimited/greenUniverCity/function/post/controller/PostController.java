package com.univercity.unlimited.greenUniverCity.function.post.controller;

import com.univercity.unlimited.greenUniverCity.function.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.post.dto.PostUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.post.repository.PostRepository;
import com.univercity.unlimited.greenUniverCity.function.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final ModelMapper mapper;
//    private final PostTraceService postTraceService;

    //P-1) 게시물 전부 조회 200
    @GetMapping("/all")
    public List<PostDTO> getPost() {
        return postService.findAllPost();
    }
    // P-2) 아이디로 한 개의 게시물만 조회 200
    @GetMapping("/one/{postId}")
    public List<PostDTO> getById(@PathVariable("postId") Long postId){
      return List.of( postService.postById(postId));
    };
    // P-3) 검색어로 게시물 조회 200
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Post>> searchPosts(@PathVariable String keyword) {
        List<Post> result = postService.searchByKeyword(keyword);
        return ResponseEntity.ok(result);
    }
    // P-5) 게시물 수정 200
    @PutMapping("/update/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody PostUpdateDTO dto) {
        PostResponseDTO updated = postService.postUpdate(postId, dto);
        return ResponseEntity.ok(updated);
    }
    // P-6) 게시물 삭제 204
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long PostId) {
        postService.deleteByPost(PostId);
        return ResponseEntity.noContent().build(); // 204
    }
    // P-7) 게시물 작성 201
    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponseDTO.builder().build());
    }


    // P-8) 작성자 기준 게시물 조회
    // P-9) 게시물 조회수
    // P-10) 게시물 좋아요, 싫어요

    // P-13) 게시물 신고
    // P-14) 게시물 북마크, 즐겨찾기
}








