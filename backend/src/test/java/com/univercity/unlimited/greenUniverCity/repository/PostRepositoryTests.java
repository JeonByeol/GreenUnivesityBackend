package com.univercity.unlimited.greenUniverCity.repository;
import com.univercity.unlimited.greenUniverCity.function.community.board.entity.Board;
import com.univercity.unlimited.greenUniverCity.function.community.board.repository.BoardRepository;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
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
    @Tag("push")
    public void testInsert(){
        // 데이터 세팅
        List<Board> boards = boardRepository.findAll();
        List<User> users = userRepository.findAll();

        // 데이터 체크
        if(boards.isEmpty() == true) {
            log.info("Board가 비어있습니다.");
            return;
        }

        if(users.isEmpty() == true) {
            log.info("User가 비어있습니다.");
            return;
        }

        String[] titles = {
                "자바 기초 강의 후기",
                "스프링 부트 실습 후기",
                "JPA 매핑이 헷갈려요",
                "출석 관리 기능 제안",
                "강의 추천해주세요!",
                "이번 학기 수강 후기",
                "교수님 강의 스타일",
                "시험 난이도 어땠나요?",
                "과제 제출 마감일 공유",
                "개발자 진로 고민 중"
        };

        String[] contents = {
                "자바 기초 강의 정말 유익했어요. 객체지향 개념이 잘 잡혔습니다.",
                "스프링 부트 실습에서 오류가 많았지만 결국 잘 해결했어요!",
                "JPA 매핑할 때 양방향 관계 설정이 너무 어렵네요. 팁 있나요?",
                "출석 관리 기능에 자동 체크 기능이 있으면 좋겠어요.",
                "백엔드 중심 강의 추천 부탁드립니다. 실무에 도움되는 걸로요!",
                "이번 학기 수강한 과목들 중에서 가장 좋았던 건 데이터베이스 수업!",
                "교수님이 설명을 천천히 해주셔서 이해가 잘 됐어요.",
                "시험이 생각보다 어려웠어요. 특히 SQL 파트가 까다로웠네요.",
                "과제 제출 마감일이 자꾸 바뀌어서 혼란스러워요. 일정 공유 부탁!",
                "프론트엔드랑 백엔드 중에 어떤 진로가 더 좋을까요? 고민돼요."
        };

        for(int i=0; i<titles.length; i++){
            int viewCount = (int)(Math.random()*100);
            User user = users.get((int)(Math.random()*users.size()));
            Board board = boards.get(boards.size()-1);

            Post post = Post.builder()
                    .title(titles[i])
                    .content(contents[i])
                    .createAt(LocalDateTime.now())
                    .viewCount(viewCount)
                    .board(board)
                    .user(user)
                    .build();

            postRepository.save(post);
        }
    }
    @Test
    public void testRead(){
        List<Post> result = postRepository.findAll();
        log.info("hello={}",result);
    }
}
