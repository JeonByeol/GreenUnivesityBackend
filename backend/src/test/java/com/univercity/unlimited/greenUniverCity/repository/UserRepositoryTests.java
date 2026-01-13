package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.department.repository.DepartmentRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder encoder;

    private String getRandomStudentNumber() {
        LocalDate now = LocalDate.now();

        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        String rand = String.format("%04d", (int)(Math.random() * 10000));
        return year + month + rand;
    }

    @Test
    @Tag("push")
    public void testInsertData() {
        List<Department> departments = departmentRepository.findAll();

        String[] emails = {
                "alice@aaa.com", "bob@aaa.com", "charlie@aaa.com", "diana@aaa.com", "edward@aaa.com",
                "fiona@aaa.com", "george@aaa.com", "hannah@aaa.com", "ian@aaa.com", "julia@aaa.com",
                "root@aaa.com", "student@aaa.com", "professor@aaa.com",
                "minji.kim@aaa.com", "taeyoung.lee@aaa.com", "soojin.park@aaa.com", "hyunwoo.choi@aaa.com", "jisoo.yoon@aaa.com",
                "donghyun.jung@aaa.com", "seoyeon.han@aaa.com", "gyuri.kang@aaa.com", "yuna.song@aaa.com", "jaemin.kim@aaa.com",
                "eunji.cho@aaa.com", "sangmin.ryu@aaa.com", "hyejin.oh@aaa.com", "junho.bae@aaa.com", "nayoung.lim@aaa.com",
                "seungwoo.kim@aaa.com", "harin.lee@aaa.com", "yujin.park@aaa.com"
        };

        String[] nicknames = {
                "알리쌤", "밥이", "찰리왕자", "디디", "에디",
                "피오나공주", "조지형", "한나누나", "이안", "줄리아",
                "루트", "학생", "교수",
                "민지", "태영", "수진", "현우", "지수",
                "동현", "서연", "규리", "유나", "재민",
                "은지", "상민", "혜진", "준호", "나영",
                "승우", "하린", "유진"
        };


        // 데이터 추가
        for (int i = 0; i < 30; i++) {
            int roleNumber = 0;
            User user = null;

            if (i == 10) { // root
                user = User.builder()
                        .email("root@aaa.com")
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();
                roleNumber = 20;
            } else if (i == 11) { // student
                user = User.builder()
                        .email("student@aaa.com")
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();
                roleNumber = 3;
            } else if (i == 12) { // professor
                user = User.builder()
                        .email("professor@aaa.com")
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();
                roleNumber = 14;
            } else { // 나머지 랜덤
                user = User.builder()
                        .email(emails[i])
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();
                roleNumber = (int) (Math.random() * 20) + 1;
            }

            user.setUserRole(UserRole.GUEST);
            user.setUserRole(UserRole.STUDENT);
            if (roleNumber > 13) user.setUserRole(UserRole.PROFESSOR);
            if (roleNumber > 19) user.setUserRole(UserRole.ADMIN);

            // ✅ 학과 랜덤 매핑
            Department dept = departments.get(
                    (int) (Math.random() * departments.size())
            );
            user.setDepartment(dept);
            user.setStudentNumber(getRandomStudentNumber());

            userRepository.save(user);
        }
    }

    @Test
    public void testEamil(){
        User a = userRepository.getUserByEmail("user9@aaa.com");
        log.info("user= {}",a);
        log.info("user roles= {}", a.getUserRole());
    }
}
