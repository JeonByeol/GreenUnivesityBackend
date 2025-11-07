package com.univercity.unlimited.greenUniverCity.repository;
import com.univercity.unlimited.greenUniverCity.entity.*;
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
public class PostRepositoryTests {

    @Autowired private PostRepository postRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;

    @Test
    public void testInsert(){
        List<UserVo> users = userRepository.findAll();
//        List<Board> board = boardRepository.findAll();
        Board board = boardRepository.getReferenceById(1L);
        users.forEach(u->{
                log.info("{}",u);
                for(int i = 0; i<20; i++){
                    Post dto = Post.builder()
//                            .board(board)
//                            .user(u)
                            .userId(null)
                            .title("title"+i)
                            .content("content"+i)
                            .createAt(LocalDateTime.now())
                            .viewCount(i)
                            .build();
                    postRepository.save(dto);
                }
            });
    }
    @Test
    public void testRead(){
        List<Post> result = postRepository.findAll();
        log.info("hello={}",result);
    }
}
