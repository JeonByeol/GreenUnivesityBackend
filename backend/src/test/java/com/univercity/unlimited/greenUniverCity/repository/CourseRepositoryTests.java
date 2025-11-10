package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Course;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
@Slf4j
public class CourseRepositoryTests {
    @Autowired
    CourseRepository repository;

    @Test
    public void insertInitData() {
        // 데이터 세팅
        String[] courseNameArray = {
                "자료구조","알고리즘","운영체제","데이터베이스","네트워크"
                ,"소프트웨어공학","인공지능","웹프로그래밍","캡스톤디자인"
        };
        String[] courseDescriptionArray = {
                "기초 자료 구조와 리스트, 스택, 큐, 트리, 그래프의 구현과 응용",
                "정렬, 탐색, 그리디, 분할정복, 동적계획법 등 알고리즘 설계와 분석",
                "프로세스·스레드 관리, 동기화, 메모리 관리와 스케줄링 원리",
                "관계형 데이터 모델, SQL, 인덱스, 트랜잭션 및 성능 최적화",
                "네트워크 계층 모델, 프로토콜, 소켓 프로그래밍 및 네트워크 설계",

                "소프트웨어 개발 생명주기, 요구사항·설계·테스트·유지보수 기법",
                "인공신경망 기초, 지도/비지도 학습 알고리즘 및 실습",
                "웹 프론트엔드·백엔드 기본과 RESTful 서비스 구현 실습",
                "팀 기반 프로젝트로 실제 요구사항 분석부터 구현까지 수행"
        };

        // 추후 수정
        // 학점 기준 : 보통 3학점 , 가벼우면 2학점, 힘들면 4학점
        int[] creditsArray = {
                3, 3, 3, 3, 3,
                3, 3, 3, 4
        };

        if(repository.count() > 0){
            log.info("만일 데이터가 존재하면, 해당 로그가 출력됩니다.");
            return;
        }

        // department나 offerings는 Link테스트에서 추가
        for(int i=0; i<courseNameArray.length; i++) {
            String courseName = courseNameArray[i];
            String description = courseDescriptionArray[i];
            int credits = creditsArray[i];
            Course course = Course.builder()
                    .courseName(courseName)
                    .description(description)
                    .credits(credits)
                    .build();

            repository.save(course);
        }
    }
}
