package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
                "julia@aaa.com"
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
                "줄리아"
        };
        
        // 데이터 추가
        for(int i=0;i<10;i++){
            int roleNumber = (int)(Math.random()*8) + 1;

            UserVo user=UserVo.builder()
                    .email(emails[i])
                    .password("1111")
                    .nickname(nicknames[i])
                    .build();

            user.addRole(UserRole.GUEST);
            if(roleNumber>3) user.addRole(UserRole.STUDENT);
            if(roleNumber>5) user.addRole(UserRole.PROFESSOR);
            if(roleNumber>7) user.addRole(UserRole.ADMIN);
            userRepository.save(user);
        }
    }

    @Test
    public void testEamil(){
        UserVo a = userRepository.getUserByEmail("user9@aaa.com");
        log.info("user= {}",a);
        log.info("user roles= {}", a.getUserRoleList());
    }
}
