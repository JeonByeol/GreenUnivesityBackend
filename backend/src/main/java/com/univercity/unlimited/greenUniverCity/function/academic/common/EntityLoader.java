package com.univercity.unlimited.greenUniverCity.function.academic.common;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Assignment;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.AssignmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.assignment.repository.SubmissionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.repository.ClassroomRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.repository.EnrollmentRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeItemRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.GradeRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.repository.StudentScoreRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.repository.CourseOfferingRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.academic.review.repository.ReviewRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.section.repository.ClassSectionRepository;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.repository.TimeTableRepository;
import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.department.repository.DepartmentRepository;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityLoader {

    private final AcademicSecurityValidator validator;

    // Repositories
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final CourseOfferingRepository offeringRepository;
    private final ClassSectionRepository sectionRepository;
    private final ClassroomRepository classroomRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final GradeItemRepository gradeItemRepository;
    private final StudentScoreRepository studentScoreRepository;
    private final ReviewRepository reviewRepository;
    private final TimeTableRepository timeTableRepository;

    // =================================================================================
    //  공통 조회 메서드
    // =================================================================================

    // 만들긴 했는데 모든 repository를 참조하는 방식 때문에 사용 안함 근데 어떻게든 쓸 일 있을거 같아서 유지

    public User getUser(Long id) {
        return validator.getEntityOrThrow(userRepository, id, "사용자");
    }

    public User getUserByEmail(String email) {
        return validator.getEntityByEmailOrThrow(userRepository, email, "사용자");
    }

    public Department getDepartment(Long id) {
        return validator.getEntityOrThrow(departmentRepository, id, "학과");
    }

    public Course getCourse(Long id) {
        return validator.getEntityOrThrow(courseRepository, id, "과목");
    }

    public CourseOffering getOffering(Long id) {
        return validator.getEntityOrThrow(offeringRepository, id, "개설 강의");
    }

    public ClassSection getSection(Long id) {
        return validator.getEntityOrThrow(sectionRepository, id, "분반");
    }

    public Classroom getClassroom(Long id) {
        return validator.getEntityOrThrow(classroomRepository, id, "강의실");
    }

    public Enrollment getEnrollment(Long id) {
        return validator.getEntityOrThrow(enrollmentRepository, id, "수강신청");
    }

    public Assignment getAssignment(Long id) {
        return validator.getEntityOrThrow(assignmentRepository, id, "과제");
    }

    public Submission getSubmission(Long id) {
        return validator.getEntityOrThrow(submissionRepository, id, "과제 제출");
    }

    public Attendance getAttendance(Long id) {
        return validator.getEntityOrThrow(attendanceRepository, id, "출결");
    }

    public Grade getGrade(Long id) {
        return validator.getEntityOrThrow(gradeRepository, id, "성적");
    }

    public GradeItem getGradeItem(Long id) {
        return validator.getEntityOrThrow(gradeItemRepository, id, "평가 항목");
    }

    public StudentScore getStudentScore(Long id) {
        return validator.getEntityOrThrow(studentScoreRepository, id, "점수");
    }

    public Review getReview(Long id) {
        return validator.getEntityOrThrow(reviewRepository, id, "리뷰");
    }

    public TimeTable getTimeTable(Long id) {
        return validator.getEntityOrThrow(timeTableRepository, id, "시간표");
    }
}