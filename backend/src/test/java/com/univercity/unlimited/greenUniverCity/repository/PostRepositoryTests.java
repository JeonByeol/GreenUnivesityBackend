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

        String[] Questions = {
                "수강신청 정정 기간이 지나면 과목 변경은 절대 불가능한가요?",
                "지각이나 조퇴도 출결 점수에 포함되나요? 기준이 궁금합니다.",
                "온라인 강의는 어느 정도 시청해야 출석으로 인정되나요?",
                "중간고사 성적에 대해 이의신청을 하고 싶은데 절차가 어떻게 되나요?",
                "휴학 후 복학할 때 별도로 신청해야 하나요, 자동 처리되나요?",
                "과제 제출 기한을 하루라도 넘기면 제출 자체가 막히나요?",
                "졸업 요건 충족 여부를 시스템에서 한눈에 확인할 수 있을까요?"
        };


        String[] answers = {
                "수강신청 정정 기간이 종료된 이후에는 원칙적으로 과목 변경이 불가능합니다. 다만, 학과 사무실 또는 담당 교수님의 승인에 따라 예외적으로 처리될 수 있으니 문의해보시기 바랍니다.",
                "지각과 조퇴는 출결 점수에 반영되며, 일정 횟수 이상 누적될 경우 결석으로 처리될 수 있습니다. 정확한 기준은 강의계획서를 참고해주세요.",
                "온라인 강의는 정해진 최소 시청 시간 이상을 충족해야 출석으로 인정됩니다. 일부 강의는 퀴즈나 출석 체크 버튼을 함께 요구할 수 있습니다.",
                "성적 이의신청은 성적 공시 기간 내에 학사관리시스템을 통해 접수할 수 있으며, 담당 교수님의 검토 후 결과가 안내됩니다.",
                "휴학 기간이 종료된 경우에도 복학 신청은 반드시 본인이 직접 진행해야 합니다. 정해진 복학 신청 기간을 놓치지 않도록 주의해주세요.",
                "과제 제출 기한이 지나면 시스템상 제출이 제한됩니다. 다만 교수님이 추가 제출을 허용한 경우, 별도로 제출 기한이 열릴 수 있습니다.",
                "졸업 요건 충족 현황은 학사관리시스템의 졸업 관리 메뉴에서 확인할 수 있으며, 전공 및 교양 학점 이수 현황이 함께 표시됩니다."
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
