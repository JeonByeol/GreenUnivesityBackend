//package com.univercity.unlimited.greenUniverCity.controller;
//
//import com.univercity.unlimited.greenUniverCity.entity.Board;
//import com.univercity.unlimited.greenUniverCity.entity.Comment;
//import com.univercity.unlimited.greenUniverCity.repository.BoardRepository;
//import com.univercity.unlimited.greenUniverCity.repository.CommentRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/community/Board")
//public class BoardController {
//    BoardRepository boardRepository;
//    @GetMapping("/All")
//    public List<Board> postAllSearch(){
//        log.info("모든 게시판들의 정보 호출");
//        return (boardRepository.findAll());
//    }
//
//}
