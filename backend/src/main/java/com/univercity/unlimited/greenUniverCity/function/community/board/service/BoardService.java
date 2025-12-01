package com.univercity.unlimited.greenUniverCity.function.community.board.service;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.LegacyBoardDTO;

import java.util.List;

public interface BoardService {
    // 전체 조회 (기존 페이지들에서 LegacyBoardDTO 쓰고 있으면 유지)
    List<LegacyBoardDTO> findAllBoard();

    // 단건 조회
    LegacyBoardDTO findIdBoard(Long boardId);

    // 생성
    BoardResponseDTO createBoard(BoardCreateDTO dto);

    // 수정 (달링이 원하는: body 에서 boardId 받는 방식)
    BoardResponseDTO updateBoard(BoardUpdateDTO dto);

    // 삭제
    void deleteBoard(Long boardId);
}
