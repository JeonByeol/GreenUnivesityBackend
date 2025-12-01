package com.univercity.unlimited.greenUniverCity.function.community.notice.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.academic.course.repository.CourseRepository;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.entity.Notice;
import com.univercity.unlimited.greenUniverCity.function.community.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper mapper;

    // N-1) 전체 조회
    @Override
    public List<NoticeResponseDTO> findAllNotice() {
        List<Notice> list = noticeRepository.findAll();

        return list.stream()
                .map(n -> NoticeResponseDTO.builder()
                        .noticeId(n.getNoticeId())
                        .title(n.getTitle())
                        .content(n.getContent())
                        .build())
                .toList();
    }

    // (필요하면 구현) 과목 일부 검색 - 아직 미사용이라 빈 구현
//    @Override
//    public List<Course> findPartCourse(String courseId, String courseName) {
//        return List.of();
//    }

    // N-2) 단일 조회
    @Override
    public List<NoticeResponseDTO> findNotice(Long noticeId) {
        Notice n = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("해당 공지가 존재하지 않습니다."));

        return List.of(NoticeResponseDTO.builder()
                .noticeId(n.getNoticeId())
                .title(n.getTitle())
                .content(n.getContent())
                .build());
    }

    @Override
    public NoticeResponseDTO updateNotice() {
        return null;
    }

    // N-3) 수정 (조건 A: Body로 noticeId 포함해서 받는 방식)
    @Override
    @Transactional
    public NoticeResponseDTO updateNotice(NoticeUpdateDTO dto) {

        // 1) 기존 notice 찾기
        Notice notice = noticeRepository.findById(dto.getNoticeId())
                .orElseThrow(() -> new RuntimeException("공지글이 없습니다."));

        // 2) 변경할 필드만 세팅 (user는 건드리지 X)
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());

        // 3) save는 선택 (영속 상태면 변경감지로 자동 반영)
        Notice saved = noticeRepository.save(notice);

        return mapper.map(saved, NoticeResponseDTO.class);
    }


    // (이름이 이상하지만, 인터페이스 맞추려고 놔둠)
    @Override
    public List<Course> findPartNotice(String noticeId, String noticeName) {
        return List.of();
    }

    // N-4) 삭제
    @Override
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    // N-5) 생성
    @Override
    public NoticeResponseDTO createNotice(NoticeCreateDTO dto) {
        Notice notice = mapper.map(dto, Notice.class);
        notice.setCreatedAt(LocalDateTime.now());
        Notice saved = noticeRepository.save(notice);
        return mapper.map(saved, NoticeResponseDTO.class);
    }
}
