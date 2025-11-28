package com.univercity.unlimited.greenUniverCity.function.notice.service;

import com.univercity.unlimited.greenUniverCity.function.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.dto.NoticeUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.notice.entity.Notice;
import com.univercity.unlimited.greenUniverCity.function.notice.repository.NoticeRepository;
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
    public NoticeResponseDTO updateNotice(NoticeUpdateDTO dto) {
        Notice entity = noticeRepository.findById(dto.getNoticeId())
                .orElseThrow(() -> new RuntimeException("공지 없음"));

        // dto -> entity 덮어쓰기 (id는 유지)
        mapper.map(dto, entity);

        Notice saved = noticeRepository.save(entity);
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
