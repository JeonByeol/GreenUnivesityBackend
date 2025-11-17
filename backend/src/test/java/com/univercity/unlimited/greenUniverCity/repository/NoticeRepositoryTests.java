package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.notice.entity.Notice;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.notice.repository.NoticeRepository;
import com.univercity.unlimited.greenUniverCity.function.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Slf4j
@Transactional
@Commit
public class NoticeRepositoryTests {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    private final Random random = new Random();

    @Test
    @Tag("push")
    public void testInsertData() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new IllegalStateException("User 데이터가 없습니다. 최소 1명 이상 필요합니다!");
        }

        User user = users.get(0); // 첫 번째 사용자 기준으로 Notice 등록

        String[] titles = {
                "공지사항", "업데이트 안내", "서비스 점검 예정", "이벤트 알림",
                "신규 기능 출시", "시스템 개선 공지", "보안 패치 안내",
                "사용자 안내문", "임시 점검 공지", "중요 안내사항"
        };

        String[] contents = {
                "오늘 오후 2시부터 서버 점검이 있습니다.",
                "새로운 기능이 추가되었습니다.",
                "시스템 안정화를 위한 업데이트가 진행됩니다.",
                "이벤트에 참여해주셔서 감사합니다.",
                "보안 관련 패치가 적용되었습니다.",
                "일부 기능의 오류가 수정되었습니다.",
                "서비스 이용에 불편을 드려 죄송합니다.",
                "신규 알림 시스템이 적용되었습니다.",
                "데이터 백업 작업이 완료되었습니다.",
                "다음 버전에서 새로운 기능이 추가될 예정입니다."
        };

        for (int i = 0; i < 10; i++) {
            Notice notice = Notice.builder()
                    .user(users.get(random.nextInt(users.size())))
                    .title(titles[random.nextInt(titles.length)] + " #" + (i + 1))
                    .content(contents[random.nextInt(contents.length)])
                    .createdAt((LocalDateTime.now().minusDays(random.nextInt(10))))
                    .build();

            noticeRepository.save(notice);
        }

        log.info("랜덤 Notice 10건 현재 총 개수: {}", noticeRepository.count());
    }
}

