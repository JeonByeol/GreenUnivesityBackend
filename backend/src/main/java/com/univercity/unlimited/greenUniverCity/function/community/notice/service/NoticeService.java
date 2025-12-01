package com.univercity.unlimited.greenUniverCity.function.community.notice.service;

import com.univercity.unlimited.greenUniverCity.function.academic.course.entity.Course;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeUpdateDTO;

import java.util.List;

public interface NoticeService {

    // N-1 전체 공지
    List<NoticeResponseDTO> findAllNotice();

    // N-2 단일 공지
    List<NoticeResponseDTO> findNotice(Long noticeId);

    // N-3 수정
    NoticeResponseDTO updateNotice();

    // N-3) 수정 (조건 A: Body로 noticeId 포함해서 받는 방식)
    NoticeResponseDTO updateNotice(NoticeUpdateDTO dto);

    List<Course> findPartNotice(String noticeId, String noticeName);

    // N-4 삭제
    void deleteNotice(Long noticeId);

    // N-5 생성
    NoticeResponseDTO createNotice(NoticeCreateDTO dto);
}
