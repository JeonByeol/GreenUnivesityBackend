package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.entity.Course;
import com.univercity.unlimited.greenUniverCity.entity.Notice;
import com.univercity.unlimited.greenUniverCity.repository.AttendanceRepository;
import com.univercity.unlimited.greenUniverCity.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper mapper;

    @Override
    public List<NoticeDTO> findAllNotice() {
        List<NoticeDTO> dto=new ArrayList<>();
        for(Notice i:noticeRepository.findAll()){
            NoticeDTO r=mapper.map(i,NoticeDTO.class);
            dto.add(r);
        }
        log.info("모든과정을 조회");
        return dto;
    }

//    public List<Course> findPartCourse(String course_id, String course_name) {
//       log.info("해당 강의를 조회");
//       return  courseRepository.findAll();
//    }
//
//    @Override
//    public ResponseEntity<String> addNotice(NoticeDTO noticeDTO) {
//        return null;
//    }
}
