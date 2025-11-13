package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.BoardDTO;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository repository;

    private final ModelMapper mapper;

    @Override
    public List<BoardDTO> findAllBoard() {
       List<BoardDTO> dto=new ArrayList<>();
       for(Board i:repository.findAll()){
           BoardDTO r=mapper.map(i,BoardDTO.class);
           dto.add(r);
       }
       return dto;
    }

//    @Override
//    public Optional<List<BoardDTO>> findAllBoard() {
//        List<Board> boards = repository.findAll();
//        List<BoardDTO> boardDTOS = boards.stream().map(board ->
//                mapper.map(board, BoardDTO.class)).toList();
//
//        Optional<List<BoardDTO>> optionalBoardDTOS = Optional.of(boardDTOS);
//        return optionalBoardDTOS;
//    }
}
