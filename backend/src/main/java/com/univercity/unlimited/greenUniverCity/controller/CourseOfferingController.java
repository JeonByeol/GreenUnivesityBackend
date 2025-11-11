package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.AttendanceDTO;
import com.univercity.unlimited.greenUniverCity.dto.CourseDTO;
import com.univercity.unlimited.greenUniverCity.dto.CourseOfferingDTO;
import com.univercity.unlimited.greenUniverCity.entity.CourseOffering;
import com.univercity.unlimited.greenUniverCity.service.CourseOfferingService;
import com.univercity.unlimited.greenUniverCity.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/courseoffering")
public class CourseOfferingController {
    private final CourseOfferingService courseOfferingService;

    @GetMapping("/all")
    public ResponseEntity<List<CourseOfferingDTO>> postmanTestCourseOffering(){
        log.info("모든 CourseOffering들의 이름 출력");

        Optional<List<CourseOfferingDTO>> optionalCourseOfferingDTOList = courseOfferingService.findAllCourseOfferingDTO();
        if(optionalCourseOfferingDTOList.isEmpty() == true){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(optionalCourseOfferingDTOList.get());
    }
}
