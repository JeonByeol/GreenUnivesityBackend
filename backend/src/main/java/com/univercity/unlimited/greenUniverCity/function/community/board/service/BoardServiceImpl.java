package com.univercity.unlimited.greenUniverCity.function.community.board.service;

import com.univercity.unlimited.greenUniverCity.function.board.dto.BoardCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.dto.LegacyBoardDTO;
import com.univercity.unlimited.greenUniverCity.function.community.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.community.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;
    private final ModelMapper mapper;

    // 1) 전체 조회
    @Override
    public List<LegacyBoardDTO> findAllBoard() {
        return repository.findAll()
                .stream()
                .map(board -> mapper.map(board, LegacyBoardDTO.class))
                .toList();
    }

    // 2) 단건 조회
    @Override
    public LegacyBoardDTO findIdBoard(Long boardId) {
        Board board = repository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));
        return mapper.map(board, LegacyBoardDTO.class);
    }

    // 3) 생성 (create)
    @Override
    public BoardResponseDTO createBoard(BoardCreateDTO dto) {
        // DTO -> 엔티티 (수동 매핑)
        Board entity = Board.builder()
                .boardName(dto.getBoardName())
                .build();

        Board saved = repository.save(entity);

        // 엔티티 -> 응답 DTO
        return BoardResponseDTO.builder()
                .boardId(saved.getBoardId())
                .boardName(saved.getBoardName())
                .build();
    }

    // 4) 수정 (update) - body 에서 boardId 받는 방식
    @Override
    public BoardResponseDTO updateBoard(BoardUpdateDTO dto) {
        // 1) id 확인
        Long boardId = dto.getBoardId();
        if (boardId == null) {
            throw new RuntimeException("boardId가 없습니다.");
        }

        // 2) 엔티티 조회
        Board entity = repository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));

        // 3) 값 변경
        entity.setBoardName(dto.getBoardName());

        // 4) 저장
        Board saved = repository.save(entity);

        // 5) 응답 DTO
        return BoardResponseDTO.builder()
                .boardId(saved.getBoardId())
                .boardName(saved.getBoardName())
                .build();
    }

    // 5) 삭제
    @Override
    public void deleteBoard(Long boardId) {
        repository.deleteById(boardId);
    }
}
