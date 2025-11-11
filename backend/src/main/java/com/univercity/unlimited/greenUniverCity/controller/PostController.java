package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.dto.PostDTO;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.entity.Post;
import com.univercity.unlimited.greenUniverCity.service.CommentService;
import com.univercity.unlimited.greenUniverCity.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.ReadPendingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/Post")
public class PostController {
    private final PostService postService;

    @GetMapping("/all")
    public List<PostDTO> getPost() {
        return postService.findAllPost();
    }

//    @GetMapping("/read/{cid}")
//    public ResponseEntity<PostDTO> getReadCommentByCid(@PathVariable("cid") Long PostId) {
//        log.info("Controller: /community/read 호출");
//        PostDTO result = postService.findAllPost(PostDTO->postService.findByIdPost(PostId));
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/list/Search")
//    public ResponseEntity<List<CommentDTO>> getListPosts() {
//        log.info("1) Controller: /community/list 호출");
//        List<CommentDTO> result  = postService.findByIdPost();
//        log.info("4) Controller: /community,{}" ,result);
//        return ResponseEntity.ok(result);
//    }
}

