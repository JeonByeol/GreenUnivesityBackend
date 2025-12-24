package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.controller;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    private String getEmail(String requesterEmail) {
        String email = requesterEmail;

        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        return email;
    }
    // p-1) postId를 기준으로 파일 확인
    @GetMapping("/all")
    public ResponseEntity<List<FileResponseDTO>> getAllFiles() {
        log.info("전체 파일 조회 요청");

        List<FileResponseDTO> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    // p-2) 파일 업로드
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<FileResponseDTO>> createFiles(
            @RequestPart("meta") FileCreateDTO meta,
            @RequestPart("files") List<MultipartFile> files
    ) {
        log.info("meta: {}", meta);
        log.info("filesCount: {}", files.size());

        List<FileResponseDTO> result = fileService.createdfile(meta.getPostId(), meta.getPostId());
        return ResponseEntity.ok(result);
    }
    // P-3 ) 파일 삭제
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable("fileId") Long fileId,
            @RequestHeader(value = "X-User-Email", required = false) String requesterEmail
    ) {
        log.info("file 삭제 요청 - fileId: {}", fileId);

        String email = getEmail(requesterEmail);

//        Map<String, String> result = fileService.deleteFile(file, filedId);
//        return ResponseEntity.ok(result.get("result"));
        fileService.deleteFile(null,fileId);
        return ResponseEntity.ok("성공");
    }
    // P-4) 다수의 파일 중 원하는 파일만 삭제하는 기능 구현하고 싶은데 어려울 거 같아서 보류합니다.
    // P-5 ) 파일 수정 (서비스 시그니처에 맞게)
    @PutMapping("/update/{fileId}")
    public ResponseEntity<FileResponseDTO> updateFile(
            @PathVariable("fileId") Long fileId,
            @RequestBody FileUpdateDTO dto
    ) {
        log.info("file 수정 요청 - fileId: {}", fileId);

        FileResponseDTO result = fileService.updateFile(fileId, dto);
        return ResponseEntity.ok(result);
    }
}
