package com.univercity.unlimited.greenUniverCity.function.community.board.controller;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.LegacyBoardDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.community.board.repository.BoardRepository;
import com.univercity.unlimited.greenUniverCity.function.community.board.service.BoardService;
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
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final ModelMapper mapper;
    private final BoardRepository repository;

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
    public ResponseEntity<BoardResponseDTO> createBoard(
            @RequestBody BoardCreateDTO dto
    ) {
        BoardResponseDTO created = boardService.createBoard(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        // 또는 return ResponseEntity.ok(created);
    }


    // B-4) BOARD 수정
    @PutMapping("/update")
    public ResponseEntity<BoardResponseDTO> updateBoard(@RequestBody BoardUpdateDTO dto) {

        // dto.boardId 안에 수정할 boardId가 들어 있어야 함
        BoardResponseDTO updated = boardService.updateBoard(dto);
        return ResponseEntity.ok(updated);
    }


    // B-5) BOARD 삭제

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Board> deleteBoard(@PathVariable("id") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build(); // 204
    }
}

