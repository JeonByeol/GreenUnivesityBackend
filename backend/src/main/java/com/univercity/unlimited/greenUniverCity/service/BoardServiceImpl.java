package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.dto.BoardDTO;
import com.univercity.unlimited.greenUniverCity.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.repository.BoardRepository;
import com.univercity.unlimited.greenUniverCity.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository repository;

    private final ModelMapper mapper;

    @Override
    public Optional<List<BoardDTO>> findAllBoard() {
        List<Board> boards = repository.findAll();
        List<BoardDTO> boardDTOS = boards.stream().map(board ->
                mapper.map(board, BoardDTO.class)).toList();

        Optional<List<BoardDTO>> optionalBoardDTOS = Optional.of(boardDTOS);
        return optionalBoardDTOS;
    }
}
