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
    //notice는 공지사항,각 강의의 휴강공지,학과.기타 공지 등등을 알림(학생은 읽기만 가능)
    //받거나 보내기
    
    //받아야 할것
    //1.모든 강의의 정보
    //2.각 학과의 정보
    //3.모든 교수의 정보
    //4.공지사항의 제목과 내용
    
    //연결되어야 할 데이터
    //1.각 강의와 해당 교수에 대한 정보
    //2.각 학과와 해당 학과교수들에 대한 정보
    //위의 것들과 연결된 공지사항 제목,내용
    
    //보내야 할것
    //1.각 강의의 정보
    //2.각 학과의 정보
    //3.각 교수의 정보
    //4.각 학과,교수,공지사항 제목,내용/강의,교수,공지사항 제목,내용 등의 연결된 것
    
    
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
