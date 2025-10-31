package com.univercity.unlimited.greenUniverCity.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ReTestVoRepositoryTest {
    @Autowired
    private ReTestRepositroy t;

    @Test
    public void testadd(){
        t.findAll();
        log.info("tno:{}",t.findAll());
    }
}
