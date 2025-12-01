package com.univercity.unlimited.greenUniverCity.function.community.notice.controller;

import com.univercity.unlimited.greenUniverCity.function.community.notice.dto.NoticeDTO;
import com.univercity.unlimited.greenUniverCity.function.community.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    // N-1) 전체 공지 조회
    @GetMapping("/all")
    public ResponseEntity<List<NoticeResponseDTO>> getAllNotices() {
        log.info("모든 공지 정보 호출");
        List<NoticeResponseDTO> result = noticeService.findAllNotice();
        return ResponseEntity.ok(result);
    }

    // N-2) 공지 하나 조회 (noticeId 기준)
    @GetMapping("/one/{noticeId}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticeById(@PathVariable Long noticeId) {
        log.info("단일 공지 조회 요청: id={}", noticeId);
        List<NoticeResponseDTO> notice = noticeService.findNotice(noticeId);
        return ResponseEntity.ok(notice);
    }

    // N-3) 공지 수정
    @PutMapping("/update")
    public ResponseEntity<NoticeResponseDTO> updateNotice(
            @RequestBody NoticeUpdateDTO dto
    ) {
        NoticeResponseDTO result = noticeService.updateNotice(dto); // ★ dto 넘겨야 함
        return ResponseEntity.ok(result);
    }


    // N-4) 공지 삭제
    @DeleteMapping("/delete/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        log.info("공지 삭제 요청: id={}", noticeId);
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // N-5) 공지 생성
    @PostMapping("/create")
    public ResponseEntity<NoticeResponseDTO> createNotice(
            @RequestBody NoticeCreateDTO dto
    ) {
        NoticeResponseDTO result = noticeService.createNotice(dto);
        return ResponseEntity.ok(result);
    }

}
