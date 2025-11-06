package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.entity.Notice;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
@Commit
public class NoticeRepositoryTests {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private UserRepository userRepository;


   @Test
   public void testInsertData() {
       List<UserVo> user = userRepository.findAll();
       UserVo r=user.get(0);

       for(int i=0;i<1;i++){
           Notice notice= Notice.builder()
                   .user(r)
                   .title("새로운 알림"+i)
                   .content("전달사항입니다")
                   .created_at(LocalDateTime.now())
                   .build();
           noticeRepository.save(notice);
       }
   }

}
