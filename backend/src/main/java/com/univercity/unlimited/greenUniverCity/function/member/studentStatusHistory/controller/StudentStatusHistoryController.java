package com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.controller;

import com.univercity.unlimited.greenUniverCity.function.member.studentStatusHistory.service.StudentStatusHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class StudentStatusHistoryController {
    private final StudentStatusHistoryService studentStatusHistoryService;

    public String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "root@aaa.com";
        }

        return email;
    }

    // H-1) StudentStatusHistory 테이블에 존재하는 모든 데이터 조회

    // H-2) StudentStatusHistory의 id를 통해 특정 데이터를 조회

    // H-3) StudentStatusHistoryCreateDTO를 통해 데이터 생성

    // H-4) StudentStatusHistoryUpdateDTO를 통해 데이터 갱신

    // H-5) StudentStatusHistory의 id를 통해 특정 데이터 삭제

}
