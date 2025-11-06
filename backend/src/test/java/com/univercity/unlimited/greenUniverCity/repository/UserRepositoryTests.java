package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsertData(){
        for(int i=0;i<10;i++){
            UserVo user=UserVo.builder()
                    .email("user"+i+"@aaa.com")
                    .password("1111")
                    .nickname("user"+i)
                    .id("user"+i)
                    .build();
            user.addRole(UserRole.ADMIN);
            if(i>3) user.addRole(UserRole.STUDENT);
            if(i>5) user.addRole(UserRole.PROFESSOR);
            if(i>7) user.addRole(UserRole.GUEST);
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
