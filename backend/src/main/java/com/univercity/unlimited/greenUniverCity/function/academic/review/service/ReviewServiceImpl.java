package com.univercity.unlimited.greenUniverCity.function.academic.review.service;

import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.LegacyReviewDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.DuplicateReviewException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.ReviewNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.UnauthorizedReviewException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.repository.ReviewRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository repository;

    private final EnrollmentService enrollmentService;

    private final ModelMapper mapper;

    /**
     * R-A) Review 엔티티를 (Response)DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위해 함수로 생성
     * | (** 추후 이 기능을 담당하는 프론트 작업시 유의할 점 **)
     * 생성 시에는 updatedAt이 null로 반환되고, 수정 시에는 값이 존재하는데
     * 프론트엔드에서 updatedAt이 null이 아닌 경우에만 "수정됨" 표시를 하면 된다
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
                .updatedAt(review.getUpdatedAt())
                .courseName(courseOffering.getCourseName())
                .studentNickname(user!= null ? user.getNickname() : "탈퇴한 사용자")
                .build();
    }

    /**
     * R-3-1) 보안 검사:
     * -로그인한 학생이 해당 수강신청의 주인인지 확인하는 함수
     */

    private void validateStudentOwnership(Enrollment enrollment, String studentEmail) {
        String enrollmentOwnerEmail = enrollment.getUser().getEmail();

        if (!enrollmentOwnerEmail.equals(studentEmail)) {
            log.warn("권한 없는 리뷰 작성 시도 - 로그인: {}, 수강신청 주인: {}",
                    studentEmail, enrollmentOwnerEmail);
            throw new UnauthorizedReviewException(
                    "4) 보안 검사 시도 식별코드 -:R-3-1" +
                            "본인이 수강한 과목에만 리뷰를 작성할 수 있습니다.");
        }

        log.info("4) 권한 검증 통과 - 학생: {}", studentEmail);
    }

    /**
     * R-3-2) 중복 리뷰 검사: 이미 해당 수강신청에 리뷰를 작성했는지 확인하는 함수
     */

    private void validateDuplicateReview(Enrollment enrollment) {
        boolean reviewExists = repository.existsByEnrollment(enrollment);

        if (reviewExists) {
            log.warn("중복 리뷰 작성 시도 - enrollmentId: {}", enrollment.getEnrollmentId());
            throw new DuplicateReviewException(
                    "4) 보안 검사 시도 식별코드 -:R-3-2" +
                            "이미 해당 과목에 대한 리뷰를 작성하셨습니다.");
        }

        log.info("4) 중복 검사 통과 - enrollmentId: {}", enrollment.getEnrollmentId());
    }

    /**
     * R-4-1) 리뷰 소유권 검사: 로그인한 학생이 해당 리뷰의 작성자인지 확인하는 함수
     */
    private void validateReviewOwnership(Review review, String studentEmail) {
        String reviewOwnerEmail = review.getEnrollment().getUser().getEmail();

        if (!reviewOwnerEmail.equals(studentEmail)) {
            log.warn("리뷰 수정 권한 없음 - 요청자: {}, 작성자: {}",
                    studentEmail, reviewOwnerEmail);
            throw new UnauthorizedReviewException(
                    "4) 보안 검사 시도 식별코드 -:R-4-1" +
                    "본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        log.info("4) 리뷰 소유권 검증 통과 - 학생: {}, reviewId: {}", studentEmail, review.getReviewId());
    }


    //R-1) 리뷰 테이블에 존재하는 모든 데이터를 조회하는 서비스 구현부
    //->60% 완료
    //구상:modelMapper를 좀 더 깔끔하거나 좋은 방식으로 수정하거나 mapper를 사용 안 할 예정
    @Transactional
    @Override
    public List<LegacyReviewDTO> findAllReview() {
        int cnt=0;
        log.info("2) 여기는 전체 리뷰 조회 service입니다 service ");
        List<LegacyReviewDTO> dtoList=new ArrayList<>();
        for(Review i:repository.findAll()){
            LegacyReviewDTO r=mapper.map(i, LegacyReviewDTO.class);
            log.info("5 ) cnt= {} 여기는 전체 리뷰 조회 service입니다 service,{}  " , cnt++ , i);
            dtoList.add(r);
        }
        return dtoList;
    }

    //R-2)특정 과목에 대해 존재하는 리뷰 목록 조회 서비스 구현부
    // -> 98.9% 완료 나중에 구상 혹은 프론트앤드에서 작업할 때 추가 수정 하거나 그대로 사용 하면 될 듯?
    @Override
    public List<ReviewResponseDTO> findOfferingForReview(Long offeringId) {
        List<Review> reviews=repository.findReviewsByCourseOfferingId(offeringId);
        
        return reviews.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //R-3) 학생이 수강중이거나 완료한 과목에 대한 리뷰를 작성하는 서비스 구현부
    // -> 90% 완료? 나중에 보안security를 추가하면 그거에 맞춰서 보안 작업에 대한 부분만 리팩토링 하면 됨
    @Override
    public ReviewResponseDTO createReviewStudent(ReviewCreateDTO dto,String studentEmail) {
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
    //-> 90% 완료? 나중에 보안security를 추가하면 그거에 맞춰서 보안 작업에 대한 부분만 리팩토링 하면 됨
    @Override
    public ReviewResponseDTO myReviewUpdate(Integer reviewId, ReviewUpdateDTO dto, String studentEmail) {

        log.info("2)리뷰 수정 시작 - reviewId: {}, 학생: {}", reviewId, studentEmail);

        //1.리뷰 조회
        Review review = repository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "3) 보안 검사 시도 식별코드 -:R-4" +
                                "리뷰가 존재하지 않습니다. reviewId: " + reviewId));

        //R-4-1)소유권 검증 (현재 로그인한 학생이 해당 리뷰의 작성자인지)
        validateReviewOwnership(review, studentEmail);

        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setUpdatedAt(LocalDateTime.now());

        Review updateReview=repository.save(review);

        log.info("5) 리뷰 수정 성공 -학생:{}, reviewId: {},수강평:{},평점:{}",
                studentEmail, reviewId, dto.getComment(),dto.getRating());
        
        return toResponseDTO(updateReview);
    }

    //R-5) 학생이 작성한 리뷰를 삭제하기 위한 서비스 구현부
    // -> 삭제 기능은 구현 o 하지만 학생과 운영자를 구분해서 삭제를 하는 기능을 만들어야함
    // 구상: serviceImpl 구현부내에 userId값을 받아서 검증하는 함수를 만들어서 검증예정
    @Override
    public void  deleteByReview(Integer reviewId,String studentEmail) {
        log.info("2)리뷰 삭제 요청 -학생:{}, reviewId:{}",studentEmail,reviewId);

        //리뷰 조회
        Review review=repository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "3)보안 검사 시도 식별코드 -:R-5" +
                                "리뷰가 존재하지 않습니다. reviewId: " + reviewId));

        //R-4-1)소유권검증
        validateReviewOwnership(review,studentEmail);

        repository.delete(review);

        log.info("5) 리뷰 삭제 성공 -학생:{}, reviewId:{}",studentEmail,reviewId);
    }

    @Override//R-A) **(기능 작성 부탁드리거나/삭제 부탁드립니다) **
    public ResponseEntity<String> addReview(LegacyReviewDTO legacyReviewDTO) {
        return null;
    }
}
