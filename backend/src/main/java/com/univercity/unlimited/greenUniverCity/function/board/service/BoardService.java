package com.univercity.unlimited.greenUniverCity.function.board.service;

import com.univercity.unlimited.greenUniverCity.function.board.dto.LegacyBoardDTO;

import java.util.List;

public interface BoardService {
    List<LegacyBoardDTO> findAllBoard();

    public LegacyBoardDTO findIdBoard(Long boardId);
}
