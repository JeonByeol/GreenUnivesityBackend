package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Review;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class ReviewRepositoryTests {
    @Autowired ReviewRepository repository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;



    @Test
    @Tag("push")
    public void testReviewData(){
        // 데이터 세팅
        int [] rate={1,2,3,4,5};
        String[] dummyComments = {
                "인생 최고의 강의였습니다! 교수님 정말 감사합니다.",
                "배운 게 많아요. 과제는 좀 많았지만 남는 게 있는 수업입니다.",
                "그냥저냥... 학점 채우기용으로는 무난합니다.",
                "교수님 설명이 너무 빨라서 따라가기 벅찼습니다.",
                "시간 낭비였어요. 다음 학기엔 다른 과목 들으세요.",
                "유익한 수업이었습니다. 추천합니다!",
                "시험이 너무 어렵게 나와서 힘들었네요...",
                "팀플이 많아서 비추천. 팀원 잘 만나야 해요.",
                "평범합니다. 기대한 만큼입니다.",
                "교수님이 열정적이시고, 질문도 잘 받아주십니다.",
                "출석을 너무 깐깐하게 잡으세요. 1분 늦어도 결석 처리됩니다.",
                "학점 따기 좋은 꿀강의입니다. 로드가 적어요.",
                "PPT 자료가 부실해서 따로 공부를 많이 해야 했습니다.",
                "전공자라면 꼭 한번 들어봐야 할 수업.",
                "기대 이하였습니다. 얻어가는 게 별로 없네요.",
                "다 좋은데 시험이 너무 어려웠어요. 그래도 많이 배웁니다.",
                "교수님... 제발... 학점... A+ 받고 싶습니다.",
                "강의 내용이 알차고 좋았습니다. 비전공자도 들을만 해요.",
                "조별 과제가 학기의 절반입니다. 조원 잘못 만나면 힘듭니다.",
                "이 수업 듣고 개발에 흥미가 생겼습니다. 좋은 강의 감사합니다!"
        };

        // 데이터 체크
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        if(enrollments.isEmpty() == true)
        {
            log.info("enrollment가 없습니다.");
            return;
        }

        for(Enrollment enrollment : enrollments){
            Review review=Review.builder()
                    .rating(rate[(int)(Math.random()* rate.length)])
                    .comment(dummyComments[(int)(Math.random()*dummyComments.length)])
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .enrollment(enrollment)
                    .build();

            repository.save(review);
        }
    }
}
