package com.univercity.unlimited.greenUniverCity.repository;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
@ToString
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository repository;

    @Test
    public void testInsert(){
        var boardArray = List.of("전체게시판","자유게시판","동아리게시판","질문게시판");
        for(int i = 0; i <= 3; i ++){

            Board dto = Board.builder()
//                    .boardId("board"+i)
                    .boardName(boardArray.get(i))
                            .build();
            repository.save(dto);
        }
    }
    @Test
    public void testRead(){
        List<Board> result = repository.findAll();
        log.info("hello={}",result);
    }

//    @Test
//    public void testdelete(){
//        Long tno = 1l;
//        Optional<CommentDTO> result = repository.deleteById(tno);
//
//        CommentDTO dto = result.orElseThrow();
//
//        log.info("hello={}",dto);
//    }

//    @RequiredArgsConstructor
//    @Log4j2
//    @RequestMapping("community/club")
//    public class TestController {
//
//        private final TestService testService;
//
//        @GetMapping("{tno}")
//        public Test01 get(@PathVariable(name="tno") Long tno){
//            return testService.findById().get(tno);
//        }
//    }
}
