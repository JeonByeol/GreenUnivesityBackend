package com.univercity.unlimited.greenUniverCity.function.community.board.controller;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.LegacyBoardDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.community.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // B-1) BOARD 전체 검색
    @GetMapping("/all")
    public List<LegacyBoardDTO> postAllSearch() {
        return boardService.findAllBoard();
    }

    // B-2) BOARD 1개만 검색
    @GetMapping("/one/{id}")
    public List<LegacyBoardDTO> postIdSearch(@PathVariable("id") Long boardId) {
        return List.of(boardService.findIdBoard(boardId));
    }

    // B-3) BOARD 생성 (나중에 @PreAuthorize 붙이면 admin 전용)
    @PostMapping("/create")
    public ResponseEntity<LegacyBoardDTO> createdBoard(
            @RequestBody LegacyBoardDTO dto
    ) {
        LegacyBoardDTO created = boardService.createBoard(dto);
        return ResponseEntity.ok(created); // 200
    }


    // B-4) BOARD 수정
    @PutMapping("/update/{Id}")
    public ResponseEntity<BoardResponseDTO> updateBoard(
            @PathVariable("Id") Long boardId,
            @RequestBody BoardUpdateDTO dto
    ) {
        BoardResponseDTO updated = boardService.updateBoard(boardId,dto);
        return ResponseEntity.ok(updated); // 200 OK
        // 또는: return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // B-5) BOARD 삭제

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Board> deleteBoard(@PathVariable("id") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build(); // 204
    }
}

