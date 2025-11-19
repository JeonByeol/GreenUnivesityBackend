package com.univercity.unlimited.greenUniverCity.function.review.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.dto.EnrollmentDTO;
import com.univercity.unlimited.greenUniverCity.function.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewDTO;
import com.univercity.unlimited.greenUniverCity.function.review.dto.ReviewResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.review.exception.DuplicateReviewException;
import com.univercity.unlimited.greenUniverCity.function.review.exception.ReviewNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.review.exception.UnauthorizedReviewException;
import com.univercity.unlimited.greenUniverCity.function.review.repository.ReviewRepository;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository repository;

    private final EnrollmentService enrollmentService;

    private final ModelMapper mapper;

    /**
     * R-3-1) 보안 검사: 로그인한 학생이 해당 수강신청의 주인인지 확인하는 함수
     */
    private void validateStudentOwnership(Enrollment enrollment, String studentEmail) {
        String enrollmentOwnerEmail = enrollment.getUser().getEmail();

        if (!enrollmentOwnerEmail.equals(studentEmail)) {
            log.warn("권한 없는 리뷰 작성 시도 - 로그인: {}, 수강신청 주인: {}",
                    studentEmail, enrollmentOwnerEmail);
            throw new UnauthorizedReviewException(
                    "본인이 수강한 과목에만 리뷰를 작성할 수 있습니다.");
        }

        log.info("3) 권한 검증 통과 - 학생: {}", studentEmail);
    }
    /**
     * R-3-2) 중복 리뷰 검사: 이미 해당 수강신청에 리뷰를 작성했는지 확인하는 함수
     */
    private void validateDuplicateReview(Enrollment enrollment) {
        boolean reviewExists = repository.existsByEnrollment(enrollment);

        if (reviewExists) {
            log.warn("중복 리뷰 작성 시도 - enrollmentId: {}", enrollment.getEnrollmentId());
            throw new DuplicateReviewException(
                    "이미 해당 과목에 대한 리뷰를 작성하셨습니다.");
        }

        log.info("4) 중복 검사 통과 - enrollmentId: {}", enrollment.getEnrollmentId());
    }
    /**
     * R-3-3) Review 엔티티를 (Response)DTO로 변환 | 추후 다른 crud 기능에 사용하기 위해 함수로 생성
     */
    private ReviewResponseDTO toResponseDTO(Review review) {
        Enrollment enrollment = review.getEnrollment();
        CourseOffering courseOffering=enrollment.getCourseOffering();
        User user=enrollment.getUser();

        return ReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .courseName(courseOffering.getCourseName())
                .studentNickname(user!= null ? user.getNickname() : "존재하지 않는 사용자")
                .build();
    }


    //R-1) 리뷰 테이블에 존재하는 모든 데이터를 조회하는 서비스 구현부
    @Transactional
    @Override
    public List<ReviewDTO> findAllReview() {
        int cnt=0;
        log.info("2) 여기는 전체 리뷰 조회 service입니다 service ");
        List<ReviewDTO> dtoList=new ArrayList<>();
        for(Review i:repository.findAll()){
            ReviewDTO r=mapper.map(i,ReviewDTO.class);
            log.info("5 ) cnt= {} 여기는 전체 리뷰 조회 service입니다 service,{}  " , cnt++ , i);
            dtoList.add(r);
        }
        return dtoList;
    }

    //R-2)특정 과목에 대해 존재하는 리뷰 목록 조회 서비스 구현부
    @Override
    public List<ReviewResponseDTO> findCourseForReview(Long offeringId) {
        List<Review> reviews=repository.findReviewsByCourseOfferingId(offeringId);
        
        return reviews.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

    }

    //R-3) 학생이 수강중이거나 완료한 과목에 대한 리뷰를 작성하는 서비스 구현부
    @Override
    public ReviewResponseDTO writeReviewStudent(ReviewCreateDTO dto,String studentEmail) {
        log.info("2) 리뷰 작성 시작 - 학생: {}, enrollmentId: {}", studentEmail, dto.getEnrollmentId());

        Enrollment enrollment = enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());

        //R-3-1)보안 검사
        validateStudentOwnership(enrollment, studentEmail);

        //R-3-2)중복 리뷰 검사
        validateDuplicateReview(enrollment);

        Review review = Review.builder()
                .enrollment(enrollment)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Review saveReview =repository.save(review);

        log.info("5) 리뷰 작성 완료 reviewId: {}, 학생: {}", saveReview.getReviewId(), studentEmail);

        return toResponseDTO(saveReview);
    }

    //R-4) 학생 본인이 기존에 작성한 리뷰를 수정하는 서비스 구현부
    @Override
    public ReviewResponseDTO myReviewUpdate(Integer reviewId,ReviewCreateDTO dto) {

        Enrollment enrollment = enrollmentService.getEnrollmentEntity(dto.getEnrollmentId());
        String studentEmail=enrollment.getUser().getNickname();
        log.info("2) 리뷰 수정 시작 - 학생: {}, enrollmentId: {}",studentEmail,dto.getEnrollmentId());

        //R-4-1)보안 검사
        validateStudentOwnership(enrollment, studentEmail);

        Review review=repository.findByEnrollment_enrollmentId(dto.getEnrollmentId())
                .orElseThrow(() -> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));

        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setCreatedAt(LocalDateTime.now());

        Review updateReview=repository.save(review);

        log.info(" 성공 -학생:{},수강평:{},평점:{}",
                studentEmail,dto.getComment(),dto.getRating());
        
        return toResponseDTO(updateReview);
    }

    @Override//R-A) **(기능 작성 부탁드리거나/삭제 부탁드립니다) **
    public ResponseEntity<String> addReview(ReviewDTO reviewDTO) {
        return null;
    }
}
