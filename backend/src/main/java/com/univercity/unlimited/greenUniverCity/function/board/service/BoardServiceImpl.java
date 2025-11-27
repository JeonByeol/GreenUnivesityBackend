package com.univercity.unlimited.greenUniverCity.function.board.service;

import com.univercity.unlimited.greenUniverCity.function.board.dto.LegacyBoardDTO;
import com.univercity.unlimited.greenUniverCity.function.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository repository;

    private final ModelMapper mapper;

    @Override
    public List<LegacyBoardDTO> findAllBoard() {
       List<LegacyBoardDTO> dto=new ArrayList<>();
       for(Board i:repository.findAll()){
           LegacyBoardDTO r=mapper.map(i, LegacyBoardDTO.class);
           dto.add(r);
       }
       return dto;
    }

    @Override
    public LegacyBoardDTO findIdBoard(Long boardId){
        Optional<Board> data = repository.findById(boardId);
        LegacyBoardDTO r = mapper.map(data.get(), LegacyBoardDTO.class);
        return r;
    }

    @Override
    public LegacyBoardDTO createBoard(LegacyBoardDTO dto) {
        Board r = mapper.map(dto, Board.class);
        Board board = repository.save(r);
        LegacyBoardDTO legacyData = mapper.map(board, LegacyBoardDTO.class);
        return legacyData;
    }

    @Override
    public LegacyBoardDTO updateBoard(Long boardId, LegacyBoardDTO dto) {
        Board entity = repository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        mapper.map(dto, entity);
        repository.save(entity);
        return mapper.map(entity, LegacyBoardDTO.class);
    }

    @Override
    public void deleteBoard(Long boardId) {
        repository.deleteById(boardId);
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
