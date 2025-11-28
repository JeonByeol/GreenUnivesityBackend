package com.univercity.unlimited.greenUniverCity.repository;

import com.univercity.unlimited.greenUniverCity.function.user.repository.UserDummyRepository;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserRepositoryTests.class,

        CourseRepositoryTests.class,

        BoardRepositoryTests.class,

        DepartmentRepositoryTests.class,

        PostRepositoryTests.class,

        CourseOfferingRepositoryTests.class,

        CommentRepositoryTests.class,

        NoticeRepositoryTests.class,

        TimeTableRepositoryTests.class,

        EnrollmentRepositoryTest.class,

        ReviewRepositoryTests.class,

        GradeRepositoryTests.class,

        AttendanceRepositoryTests.class,

        UserDummyRepositoryTests.class
})
@IncludeTags("push")
public class AllStartTest {
}
