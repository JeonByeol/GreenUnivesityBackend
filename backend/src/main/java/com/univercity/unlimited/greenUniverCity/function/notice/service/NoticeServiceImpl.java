package com.univercity.unlimited.greenUniverCity.function.notice.service;

import com.univercity.unlimited.greenUniverCity.function.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.entity.Notice;
import com.univercity.unlimited.greenUniverCity.function.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
