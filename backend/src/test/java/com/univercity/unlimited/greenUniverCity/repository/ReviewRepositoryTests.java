package com.univercity.unlimited.greenUniverCity.repository;


import com.univercity.unlimited.greenUniverCity.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.entity.Review;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
@Slf4j
public class ReviewRepositoryTests {
    @Autowired ReviewRepository repository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Test
    public void testReviewData(){
            final long enrollmentId=1;
//            Enrollment enrollment=enrollmentRepository.findById(enrollmentId)
//                    .orElseThrow(() ->
//                            new RuntimeException("Test Error: Enrollment " + enrollmentId + " not found")
//                    );
            Review review=Review.builder()
                    .rating(1)
                    .comment("응")
                    .createdAt(LocalDateTime.now())
                    .enrollment(new Enrollment(1l,null,null,new Date()))
                    .build();
            repository.save(review);
//        for (int i=1; i <3;i++){
//        }
    }
}
