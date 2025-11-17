package com.univercity.unlimited.greenUniverCity.function.comment.controller;

import com.univercity.unlimited.greenUniverCity.function.comment.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.function.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
    @RequestMapping("api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/commentlist")
    public ResponseEntity<List<CommentDTO>> getListComments() {
        log.info("1) Controller: /community/list 호출");
        List<CommentDTO> result  = commentService.findList();
        log.info("4) Controller: /community,{}" ,result);
        return ResponseEntity.ok( result);
    }
//    @GetMapping("/comment/read/{cid}")
//    public ResponseEntity<CommentDTO> getReadCommentByCid( @PathVariable("cid") Long commentId) {
//        log.info("Controller: /community/read 호출");
//        CommentDTO result = commentService.findByCommentCommentId(commentId);
//
//        return ResponseEntity.ok(result);
//    }
//
}
