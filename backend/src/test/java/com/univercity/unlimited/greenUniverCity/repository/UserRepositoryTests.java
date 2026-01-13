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
    public void testInsertData(){
        List<Department> departments = departmentRepository.findAll();

        String[] emails = {
                "alice@aaa.com",
                "bob@aaa.com",
                "charlie@aaa.com",
                "diana@aaa.com",
                "edward@aaa.com",

                "fiona@aaa.com",
                "george@aaa.com",
                "hannah@aaa.com",
                "ian@aaa.com",
                "julia@aaa.com",

                "root@aaa.com"
        };

        String[] nicknames = {
                "알리쌤",
                "밥이",
                "찰리왕자",
                "디디",
                "에디",

                "피오나공주",
                "조지형",
                "한나누나",
                "이안",
                "줄리아",


                "루트",
                "학생",
                "교수"
        };

        // 데이터 추가
        for(int i=0;i<13;i++){
            int roleNumber = 0;

            User user= null;
            if(i == 10){
                user = User.builder()
                        .email("root@aaa.com")
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();

                roleNumber = 10;
            } else if(i == 11) {
                user = User.builder()
                        .email("student@aaa.com")
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();

                roleNumber = 7;
            } else if(i == 12){
                user = User.builder()
                        .email("professor@aaa.com")
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();

                roleNumber = 3;
            }
            else {
                user = User.builder()
                        .email(emails[i])
                        .password(encoder.encode("1111"))
                        .nickname(nicknames[i])
                        .build();

                roleNumber = (int)(Math.random()*10) + 1;
            }

            user.setUserRole(UserRole.GUEST);
            user.setUserRole(UserRole.STUDENT);
            if(roleNumber>6) user.setUserRole(UserRole.PROFESSOR);
            if(roleNumber>9) user.setUserRole(UserRole.ADMIN);

            // ✅ 학과 랜덤 매핑
            Department dept = departments.get(
                    (int)(Math.random() * departments.size())
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
