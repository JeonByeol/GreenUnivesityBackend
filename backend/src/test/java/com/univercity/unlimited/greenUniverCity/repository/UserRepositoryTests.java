package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Tag("push")
    public void testInsertData(){
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
                        .password("1111")
                        .nickname(nicknames[i])
                        .build();

                roleNumber = 9;
            } else if(i == 11) {
                user = User.builder()
                        .email("student@aaa.com")
                        .password("1111")
                        .nickname(nicknames[i])
                        .build();

                roleNumber = 4;
            } else if(i == 12){
                user = User.builder()
                        .email("professor@aaa.com")
                        .password("1111")
                        .nickname(nicknames[i])
                        .build();

                roleNumber = 6;
            }
            else {
                user = User.builder()
                        .email(emails[i])
                        .password("1111")
                        .nickname(nicknames[i])
                        .build();

                roleNumber = (int)(Math.random()*8) + 1;
            }

            user.setUserRole(UserRole.GUEST);
            if(roleNumber>3) user.setUserRole(UserRole.STUDENT);
            if(roleNumber>5) user.setUserRole(UserRole.PROFESSOR);
            if(roleNumber>7) user.setUserRole(UserRole.ADMIN);
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
