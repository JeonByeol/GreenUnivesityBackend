package com.univercity.unlimited.greenUniverCity.function.community.board.service;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.LegacyBoardDTO;

import java.util.List;

public interface BoardService {
    List<LegacyBoardDTO> findAllBoard();

    public LegacyBoardDTO findIdBoard(Long boardId);

    LegacyBoardDTO createBoard(LegacyBoardDTO dto);

    BoardResponseDTO updateBoard(Long boardId, BoardUpdateDTO dto);

    void deleteBoard(Long boardId);
}
