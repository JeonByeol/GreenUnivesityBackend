package com.univercity.unlimited.greenUniverCity.repository;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        // 1. 기초 데이터 (유저, 학과, 게시판, 강의실)
        UserRepositoryTests.class,

        BoardRepositoryTests.class,

        DepartmentRepositoryTests.class,

        // Classroom은 독립적이므로 앞쪽에 배치
        ClassroomRepositoryTests.class,

        // 2. 학사 데이터 (강의 -> 개설 강의)
        CourseRepositoryTests.class,

        PostRepositoryTests.class,

        CourseOfferingRepositoryTests.class,

        //ClassSection은 CourseOffering과 Classroom이 필요하므로 그 뒤에 배치
        ClassSectionRepositoryTests.class,


        // 3. 수강 및 활동 데이터 (개설 강의에 의존)
        CommentRepositoryTests.class,

        NoticeRepositoryTests.class,

        // TimeTable은 ClassSection에 의존하도록 변경될 예정이므로 그 뒤에 배치하는 것이 좋다(추후바꿀예정)
        TimeTableRepositoryTests.class,

        EnrollmentRepositoryTest.class,

        ReviewRepositoryTests.class,

        GradeRepositoryTests.class,

        AttendanceRepositoryTests.class,

        UserDummyRepositoryTests.class,

        FileAttachmentRepositoryTests.class,

        SearchHistoryRepositoryTests.class
})
@IncludeTags("push")
public class AllStartTest {
}
