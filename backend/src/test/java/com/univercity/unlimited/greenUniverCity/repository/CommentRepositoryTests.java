package com.univercity.unlimited.greenUniverCity.repository;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import lombok.RequiredArgsConstructor;
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
//@RequiredArgsConstructor
public class CommentRepositoryTests {

    @Autowired UserRepository userRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository repository;

    @Test
    public void testInsert(){

        userRepository.findAll().forEach(u->{
            log.info("{}",u);
            for(int i = 0; i <= 20; i ++){

                Comment dto = Comment.builder()
//                    .commentId(20l+i)
                    .content("user 내용:"+  u.getUno()+" , " +  i + "")
                    .createdAt(LocalDateTime.now())
//                    .postId(15l+i+u.getUno())
                        .post(postRepository.findById(1l).get())
                        .board( new Board(1l,"김유라 게시판"))
                    .user(u)
                    .build();
            repository.save(dto);
        }

        });
    }
    @Test
    public void testRead(){
        List<Comment> result = repository.findAll();
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
