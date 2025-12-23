package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.entity.AcademicTerm;
import com.univercity.unlimited.greenUniverCity.function.academic.academicTerm.respository.AcademicTermRepository;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistory;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.entity.StudentStatusHistoryType;
import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.repository.StudentStatusHistoryRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class StudentStatusHistoryRepositoryTests {
    @Autowired
    private StudentStatusHistoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Tag("push")
    public void testInsertData() {
        // 데이터 세팅
        List<User> userList = userRepository.findAll();

        for(int i=0; i<10; i++) {
            int maxIndex = StudentStatusHistoryType.values().length;
            int randomIndex = (int)(Math.random()*maxIndex);
            StudentStatusHistoryType randomType = StudentStatusHistoryType.values()[randomIndex];
            StudentStatusHistory history = StudentStatusHistory.builder()
                    .changeType(randomType)
                    .changeDate(LocalDate.now())
                    .reason("이유" + i)
                    .user(userList.get((int)(Math.random()*userList.size())))
                    .build();

            repository.save(history);
        }
    }

}
