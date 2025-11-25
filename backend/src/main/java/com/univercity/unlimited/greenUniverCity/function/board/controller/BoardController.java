package com.univercity.unlimited.greenUniverCity.function.board.controller;

import com.univercity.unlimited.greenUniverCity.function.board.dto.LegacyBoardDTO;
import com.univercity.unlimited.greenUniverCity.function.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    @Autowired
    private BoardService service;

    @GetMapping("/all")
    public List<LegacyBoardDTO> postAllSearch(){
        return service.findAllBoard();
    }
    // B-1) GetMapping,ALL,Id
    @GetMapping("one/{Id}")
    public ResponseEntity<LegacyBoardDTO> postIdSearch(@PathVariable("Id") Long BoardId){
        return ResponseEntity.ok(service.findIdBoard(BoardId));
    }
//    public ResponseEntity<List<BoardDTO>> postAllSearch(){
//        log.info("모든 게시판들의 이름 출력");
//
//        Optional<List<BoardDTO>> optionalBoardDTOS = service.findAllBoard();
//        if(optionalBoardDTOS.isEmpty() == true){
//            return ResponseEntity.ok(null);
//        }
//        return ResponseEntity.ok(optionalBoardDTOS.get());

}
