package com.univercity.unlimited.greenUniverCity.function.community.board.service;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;

import java.util.List;

public interface BoardService {
    // 전체 조회 (기존 페이지들에서 LegacyBoardDTO 쓰고 있으면 유지)
    List<BoardResponseDTO> findAllBoard();

    // 단건 조회
    BoardResponseDTO findIdBoard(Long boardId);

    // 생성
    BoardResponseDTO createBoard(BoardCreateDTO dto);

    // 추가
    BoardResponseDTO updateBoard(BoardUpdateDTO dto);

    // 삭제
    void deleteBoard(Long boardId);
}
