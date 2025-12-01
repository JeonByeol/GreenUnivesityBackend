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
    @PutMapping("/update")
    public ResponseEntity<PostResponseDTO> updatePost(
            @RequestBody PostUpdateDTO dto
    ) {
        PostResponseDTO result = postService.postUpdate(dto.getPostId(), dto);
        return ResponseEntity.ok(result);
    }


    // P-6) 게시물 삭제 204
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @RequestHeader(value = "X-User-Email", required = false) String studentEmail) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // 204
    }
    // P-7) 게시물 작성 201
    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateDTO dto) {
        postService.postCreate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponseDTO.builder().build());
    }

}








