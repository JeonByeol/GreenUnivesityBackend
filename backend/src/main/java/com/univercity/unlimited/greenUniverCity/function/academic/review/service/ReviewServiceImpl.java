package com.univercity.unlimited.greenUniverCity.function.academic.review.service;

import com.univercity.unlimited.greenUniverCity.function.academic.common.AcademicSecurityValidator;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.service.EnrollmentService;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.DuplicateReviewException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.exception.ReviewNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.academic.review.repository.ReviewRepository;
import com.univercity.unlimited.greenUniverCity.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional

public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository repository;
    private final EnrollmentRepository enrollmentRepository;

    private final AcademicSecurityValidator validator;
    private final EntityMapper entityMapper;

    //R-1) 리뷰 테이블에 존재하는 모든 데이터를 조회하는 서비스 구현부
    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findAllReview() {
        log.info("2) 리뷰 전체조회 시작");

        return repository.findAllWithDetails().stream()
                .map(entityMapper::toReviewResponseDTO)
                .toList();
    }

    //R-2)특정 과목에 대해 존재하는 리뷰 목록 조회 서비스 구현부
    // -> 98.9% 완료 나중에 구상 혹은 프론트앤드에서 작업할 때 추가 수정 하거나 그대로 사용 하면 될 듯?
    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> findOfferingForReview(Long offeringId) {
        log.info("2 특정 과목에 존재하는 리뷰 조회 시작 - offeringId-:{}", offeringId);
        List<Review> reviews=repository.findReviewsByCourseOfferingId(offeringId);

        log.info("3)특정 과목 리뷰 조회 성공 - offeringId-:{}",offeringId);
        return reviews.stream()
                .map(entityMapper::toReviewResponseDTO)
                .toList();
    }

    //R-2-1) 본인 id를 활용하여 단건 조회를 할 수 있는 service구현부
    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReview(Long reviewId) {
        log.info("2) 리뷰 단건 조회 시작 - reviewId-:{}", reviewId);

        Review review = getReviewOrThrow(reviewId);

        return entityMapper.toReviewResponseDTO(review);
    }

    //R-3) 학생이 수강중이거나 완료한 과목에 대한 리뷰를 작성하는 서비스 구현부
    // -> 90% 완료? 나중에 보안security를 추가하면 그거에 맞춰서 보안 작업에 대한 부분만 리팩토링 하면 됨
    @Override
    public ReviewResponseDTO createReviewStudent(ReviewCreateDTO dto,String studentEmail) {
        log.info("2) 리뷰 작성 시작 - 학생: {}, enrollmentId: {}", studentEmail, dto.getEnrollmentId());

        Enrollment enrollment = getEnrollmentOrThrow(dto.getEnrollmentId());

        // 보안 검사
        validator.validateStudentEnrollmentOwnership(enrollment,studentEmail,"리뷰작성");

        // 중복 리뷰 검사
        boolean exists = repository.existsByEnrollment(enrollment);
        validator.validateDuplicate(exists, "해당 강의의 리뷰");

        Review review = Review.builder()
                .enrollment(enrollment)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        Review saveReview =repository.save(review);
        log.info("5) 리뷰 작성 완료 reviewId: {}, 학생: {}", saveReview.getReviewId(), studentEmail);

        return entityMapper.toReviewResponseDTO(saveReview);
    }

    //R-4) 학생 본인이 기존에 작성한 리뷰를 수정하는 서비스 구현부
    //-> 90% 완료? 나중에 보안security를 추가하면 그거에 맞춰서 보안 작업에 대한 부분만 리팩토링 하면 됨
    @Override
    public ReviewResponseDTO myReviewUpdate(ReviewUpdateDTO dto, String studentEmail) {

        log.info("2)리뷰 수정 시작 - reviewId: {}, 학생: {}",
                dto.getReviewId(), studentEmail);

        // 리뷰 조회
        Review review = getReviewOrThrow(dto.getReviewId());

        // 보안검사
        validator.validateReviewOwnership(review, studentEmail,"리뷰수정");

        review.updateReviewInfo(dto.getRating(), dto.getComment());

        Review updateReview=repository.save(review);

        log.info("5) 리뷰 수정 성공 -학생:{}, reviewId: {},수강평:{},평점:{}",
                studentEmail, dto.getReviewId(), dto.getComment(),dto.getRating());

        return entityMapper.toReviewResponseDTO(updateReview);
    }

    //R-5) 학생이 작성한 리뷰를 삭제하기 위한 서비스 구현부
    // -> 삭제 기능은 구현 o 하지만 학생과 운영자를 구분해서 삭제를 하는 기능을 만들어야함
    // 구상: serviceImpl 구현부내에 userId값을 받아서 검증하는 함수를 만들어서 검증예정
    @Override
    public void  deleteByReview(Long reviewId,String studentEmail) {
        log.info("2)리뷰 삭제 요청 -학생:{}, reviewId:{}",studentEmail,reviewId);

        //리뷰 조회
        Review review = getReviewOrThrow(reviewId);

        //본인 리뷰인지 확인
        validator.validateReviewOwnership(review,studentEmail,"리뷰삭제");

        repository.delete(review);

        log.info("5) 리뷰 삭제 성공 -학생:{}, reviewId:{}",studentEmail,reviewId);
    }

    @Override
    public List<ReviewResponseDTO> getMyData(String email) {
        log.info("Service) 본인 수강 내역 조회 시작 - email: {}", email);

        List<Review> list =
                repository.findReviewsByProfessorEmail(email);

        if (list == null || list.isEmpty()) {
            log.info("Service) 조회 결과 없음 - email: {}", email);
            return List.of(); // 빈 리스트 반환
        }

        log.info("Service) 조회 완료 - email: {}, count: {}", email, list.size());
        return list.stream()
                .map(result -> entityMapper.toReviewResponseDTO(result))
                .toList();
    }

    // =========================================================================
    //  함수
    // =========================================================================
    private Review getReviewOrThrow(Long id) {
        return validator.getEntityOrThrow(repository, id, "리뷰");
    }

    private Enrollment getEnrollmentOrThrow(Long id) {
        return validator.getEntityOrThrow(enrollmentRepository, id, "수강신청");
    }
}
