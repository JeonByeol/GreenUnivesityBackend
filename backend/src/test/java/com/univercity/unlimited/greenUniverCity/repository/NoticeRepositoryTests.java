package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
public class NoticeRepositoryTests {
    @Autowired
    private NoticeRepository noticeRepository;

   @Test
   public void testInsertData() {
       for(int i=0;i<1;i++){
           Notice notice= Notice.builder()
                   .notice_id(i)
                   .userVo(null)
                   .title("새로운 알림"+i)
                   .content("전달사항입니다")
                   .created_at(LocalDateTime.now())
                   .build();
           noticeRepository.save(notice);
       }
   }

}
