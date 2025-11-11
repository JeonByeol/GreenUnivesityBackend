package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.BoardDTO;
import com.univercity.unlimited.greenUniverCity.dto.CommentDTO;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.ReadPendingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
    @RequestMapping("/community")
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
