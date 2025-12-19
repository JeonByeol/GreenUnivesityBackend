package com.univercity.unlimited.greenUniverCity.util;

import com.univercity.unlimited.greenUniverCity.function.academic.attendance.dto.AttendanceResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.attendance.entity.Attendance;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.dto.ClassroomResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.classroom.entity.Classroom;
import com.univercity.unlimited.greenUniverCity.function.academic.course.dto.CourseResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.dto.EnrollmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.entity.Enrollment;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.grade.GradeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.gradeitem.GradeItemResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.dto.studentscore.StudentScoreResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.Grade;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.dto.CourseOfferingResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.offering.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.function.academic.review.dto.ReviewResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.review.entity.Review;
import com.univercity.unlimited.greenUniverCity.function.academic.section.entity.ClassSection;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.academic.timetable.entity.TimeTable;
import com.univercity.unlimited.greenUniverCity.function.member.department.dto.DepartmentResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMapper {
    /**
     * EntityMapper-A) 각종 엔티티들을 (Response) DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 MapperUtil로 생성
     */

    // ==========================================
    // 1. Attendance 변환 (Attendance-A)
    // ==========================================
    public AttendanceResponseDTO toattendanceResponseDTO(Attendance attendance){
        if (attendance == null) return null;

        Enrollment enrollment=attendance.getEnrollment();
        // [안전장치 추가] enrollment가 null이면 user도 못 꺼내므로 방어 로직 필요
        User user = (enrollment != null) ? enrollment.getUser() : null;

        return AttendanceResponseDTO.builder()
                .attendanceId(attendance.getAttendanceId())
                .enrollmentId(enrollment != null ? enrollment.getEnrollmentId() : null) // null 체크
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .studentNickName(user != null ? user.getNickname() : "알 수 없음") // null 체크
                .build();
    }

    // ==========================================
    // 2. Classroom 변환 (Classroom-A)
    // ==========================================
    public ClassroomResponseDTO toClassroomResponseDTO(Classroom classroom){
        if (classroom == null) return null;

        return ClassroomResponseDTO.builder()
                .classroomId(classroom.getClassroomId())
                .capacity(classroom.getCapacity())
                .location(classroom.getLocation())
                .build();

    }

    // ==========================================
    // 3. Course 변환 (Course-A)
    // ==========================================
    public CourseResponseDTO toCourseResponseDTO(Course course){
        if (course == null) return null;

        Department department = course.getDepartment();

        return
                CourseResponseDTO.builder()
                        .courseId(course.getCourseId())
                        .courseName(course.getCourseName())
                        .description(course.getDescription())
                        .credits(course.getCredits())
                        .departmentId(department != null ? department.getDepartmentId() : -1)
                        .build();
    }

    // ==========================================
    // 4. Enrollment 변환 (Enrollment-A)
    // ==========================================
    public EnrollmentResponseDTO toEnrollmentResponseDTO(Enrollment enrollment) {
        if (enrollment == null) return null;

        User user = enrollment.getUser();
        ClassSection section = enrollment.getClassSection();

        return EnrollmentResponseDTO.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .sectionId(section != null ? section.getSectionId() : -1L)
                .enrollDate(enrollment.getEnrollDate())
                .userId(user != null ? user.getUserId() : -1L)
                .build();
    }

    // ==========================================
    // 5. Grade 변환 (Grade-A)
    // ==========================================
    public GradeResponseDTO toGradeResponseDTO(Grade grade){
        if (grade == null) return null;

        Enrollment enrollment = grade.getEnrollment();
        CourseOffering offering=enrollment.getClassSection().getCourseOffering();

        return GradeResponseDTO.builder()
                .gradeId(grade.getGradeId())
                .enrollmentId(enrollment.getEnrollmentId())
                .courseName(offering.getCourseName()) // 추가된 필드 매핑
                .totalScore(grade.getTotalScore())
                .letterGrade(grade.getLetterGrade())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }

    // ==========================================
    // 6. GradeItem 변환 (GradeItem-A)
    // ==========================================
    public GradeItemResponseDTO toGradeItemResponseDTO(GradeItem gradeItem){
        if (gradeItem == null) return null;

        CourseOffering offering=gradeItem.getCourseOffering();

        return GradeItemResponseDTO.builder()
                .itemId(gradeItem.getItemId())
                .offeringId(offering.getOfferingId())
                .itemName(gradeItem.getItemName())
                .itemType(gradeItem.getItemType())
                .maxScore(gradeItem.getMaxScore())
                .weightPercent(gradeItem.getWeightPercent())
                .build();
    }

    // ==========================================
    // 7. StudentScore 변환 (StudentScore-A)
    // ==========================================

    public StudentScoreResponseDTO toStudentScoreResponseDTO(StudentScore studentScore){
        if (studentScore == null) return null;

        GradeItem item=studentScore.getGradeItem();
        Enrollment enrollment=studentScore.getEnrollment();

        return StudentScoreResponseDTO.builder()
                .scoreId(studentScore.getScoreId())
                .enrollmentId(enrollment.getEnrollmentId())
                .itemId(item.getItemId())
                .scoreObtained(studentScore.getScoreObtained())
                .createdAt(studentScore.getCreatedAt())
                .updatedAt(studentScore.getUpdatedAt())
                .build();
    }

    // ==========================================
    // 8. CourseOffering 변환 (CourseOffering-A)
    // ==========================================

    public CourseOfferingResponseDTO toCourseOfferingResponseDTO(CourseOffering offering){
        if (offering == null) return null;

        // 데이터가 없는 경우를 대비합니다.
        String professorName = "공백";
        User professor = offering.getProfessor();
        if (professor != null && professor.getNickname() != null) {
            professorName = professor.getNickname();
        }

        String courseName = "공백";
        Course course = offering.getCourse();
        if(offering.getCourseName() == null) {
            if (course != null && course.getCourseName() != null) {
                courseName = course.getCourseName();
            }
        } else {
            courseName = offering.getCourseName();
        }

        return
                CourseOfferingResponseDTO.builder()
                        .offeringId(offering.getOfferingId())
                        .professorId(professor != null ? professor.getUserId() : null)
                        .professorName(professorName)
                        .courseName(courseName)
                        .year(offering.getYear())
                        .semester(offering.getSemester())
                        .courseId(course != null ? course.getCourseId() : null)
                        .build();
    }

    // ==========================================
    // 9. Review 변환 (Review-A)
    // ==========================================
    public ReviewResponseDTO toReviewResponseDTO(Review review) {
        if (review == null) return null;

        Enrollment enrollment = review.getEnrollment();
        ClassSection section=enrollment.getClassSection();
        CourseOffering courseOffering=section.getCourseOffering();
        User user=enrollment.getUser();

        return ReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .courseName(courseOffering.getCourseName())
                .studentNickname(user!= null ? user.getNickname() : "탈퇴한 사용자")
                .enrollmentId(enrollment.getEnrollmentId())
                .build();
    }
    // ==========================================
    // 10. TimeTable 변환 (TimeTable-A)
    // ==========================================

    public TimeTableResponseDTO toTimeTableResponseDTO(TimeTable timeTable){
        if (timeTable == null) return null;

        ClassSection section = timeTable.getClassSection();
        CourseOffering courseOffering = section.getCourseOffering();
        User user = courseOffering.getProfessor();
        Classroom classroom = timeTable.getClassroom();

        return TimeTableResponseDTO.builder()
                .timetableId(timeTable.getTimetableId())
                .dayOfWeek(timeTable.getDayOfWeek())
                .startTime(timeTable.getStartTime())
                .endTime(timeTable.getEndTime())
                .classroomId(classroom.getClassroomId())
                .classroomName(classroom.getLocation())
                .sectionId(section.getSectionId())
                .sectionName(section.getSectionName())
                .courseName(courseOffering.getCourseName())
                .build();
    }

    // ==========================================
    // 11. ClassSection 변환 (ClassSection-A)
    // ==========================================

    // ==========================================
    // 12. Department 변환 (Department-A)
    // ==========================================
    public DepartmentResponseDTO toDepartmentResponseDTO(Department department){
        if (department == null) return null;

        return
                DepartmentResponseDTO.builder()
                        .departmentId(department.getDepartmentId())
                        .deptName(department.getDeptName())
//                        .courseId(department.getCourses())
                        .build();
    }
}
