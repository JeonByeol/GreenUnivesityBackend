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
        for (int i=1; i <3;i++){
            final long enrollmentId=i;
            Enrollment enrollment=enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() ->
                            new RuntimeException("Test Error: Enrollment " + enrollmentId + " not found")
                    );
            Review review=Review.builder()
                    .rating(i)
                    .comment("응"+i)
                    .createdAt(LocalDateTime.now())
                    .enrollment(enrollment)
                    .build();
            repository.save(review);

        }
    }
}
