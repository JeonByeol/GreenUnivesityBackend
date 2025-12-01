package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserDummy;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserDummyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
public class UserDummyRepositoryTests {
    @Autowired
    private UserDummyRepository dummyRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @Tag("push")
    public void testDummyData(){
        String [] emails={
                "star@star.com",
                "byeol@gg.com",
                "dummy@dor.com"
        };

        String [] nicknames ={
                "전별",
                "별전",
                "더미"
        };

        UserRole [] roles={
                UserRole.STUDENT,
                UserRole.PROFESSOR,
                UserRole.ADMIN
        };

        for(int i=0;i<3;i++){
            UserDummy userDummy= UserDummy.builder()
                    .email(emails[i])
                    .password(encoder.encode("1111"))
                    .nickname(nicknames[i])
                    .role(roles[i])
                    .build();

            dummyRepository.save(userDummy);
        }

    }

}
