package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.dto.BoardDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    List<BoardDTO> findAllBoard();
}
