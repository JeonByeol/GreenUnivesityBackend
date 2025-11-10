package com.univercity.unlimited.greenUniverCity.repository;
import com.univercity.unlimited.greenUniverCity.entity.Board;
import com.univercity.unlimited.greenUniverCity.entity.Comment;
import com.univercity.unlimited.greenUniverCity.entity.Post;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
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
    @Autowired CommentRepository repository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    @Test
    public void testInsert(){
        // 데이터 세팅
        String[] comments = {
                "정말 공감되는 글이에요. 감사합니다!",
                "저도 비슷한 경험이 있었어요. 힘내세요!",
                "좋은 정보 감사합니다. 도움이 많이 됐어요.",
                "이건 좀 아닌 것 같아요... 다시 생각해보시는 게 어떨까요?",
                "질문이 있는데 혹시 자세히 설명해주실 수 있나요?",
                "와 이건 진짜 꿀팁이네요. 저장해갑니다!",
                "내용이 너무 어려워요. 쉽게 설명해주실 수 있나요?",
                "이런 글 더 자주 보고 싶어요. 응원합니다!",
                "댓글 보니까 저만 그런 게 아니었네요. 위로가 됩니다.",
                "좋은 글 감사합니다. 덕분에 많은 걸 배웠어요!"
        };

        // 데이터 체크
        List<UserVo> userVo = userRepository.findAll();
        List<Post> posts = postRepository.findAll();

        if(userVo.isEmpty() == true) {
            log.info("유저 항목이 비어있습니다.");
            return;
        }
        if(posts.isEmpty() == true) {
            log.info("Post 항목이 비어있습니다.");
            return;
        }


        userVo.forEach(u->{
            int ran = (int)(Math.random()*3)+1;
            for(int i = 0; i < ran; i ++){
                int postRan = (int)(Math.random() * posts.size());
                Comment dto = Comment.builder()
                        .content(comments[(int)(Math.random()*comments.length)])
                        .createdAt(LocalDateTime.now())
                        .user(u)
                        .post(posts.get(postRan))
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
}
