package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.service;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.entity.FileAttachment;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.repository.FileRepository;
import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import com.univercity.unlimited.greenUniverCity.function.community.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    // 일단 간단하게 문자열로 고정 경로
    private final String uploadDir = "uploads";

    private final FileRepository fileRepository;
    private final PostRepository postRepository;

    /**
     * 다중 파일 업로드 (Create)
     */
    @Override
    public List<FileResponseDTO> saveFiles(List<MultipartFile> files, Long postId) {

        // 1. post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다. postId = " + postId));

        List<FileAttachment> entityList = new ArrayList<>();

        // 2. 파일 하나씩 처리
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                throw new RuntimeException("파일명이 없습니다.");
            }

            // 확장자 추출 (없으면 빈 문자열)
            String ext = "";
            int dotIndex = originalName.lastIndexOf(".");
            if (dotIndex != -1 && dotIndex < originalName.length() - 1) {
                ext = originalName.substring(dotIndex + 1);
            }

            // 저장용 파일명 (UUID 사용)
            String storedName;
            if (ext.isEmpty()) {
                storedName = UUID.randomUUID().toString();
            } else {
                storedName = UUID.randomUUID() + "." + ext;
            }

            try {
                // 폴더 없으면 만들기
                Path dirPath = Paths.get(uploadDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                // 실제 저장 경로
                Path storedPath = dirPath.resolve(storedName);

                // 파일 저장
                Files.copy(file.getInputStream(), storedPath);

                // 엔티티 생성
                FileAttachment entity = FileAttachment.builder()
                        .post(post)
                        .originalName(originalName)
                        .storedName(storedName)
                        .storedPath(storedPath.toString())
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .build();

                entityList.add(entity);

            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생: " + originalName);
            }
        }

        // 3. DB 저장
        List<FileAttachment> savedList = fileRepository.saveAll(entityList);

        // 4. DTO 변환
        List<FileResponseDTO> result = new ArrayList<>();
        for (FileAttachment f : savedList) {
            FileResponseDTO dto = new FileResponseDTO();
            dto.setFileId(f.getId());
            dto.setPostId(f.getPost().getPostId());
            dto.setOriginalName(f.getOriginalName());
            dto.setStoredName(f.getStoredName());
            dto.setStoredPath(f.getStoredPath());
            dto.setSize(f.getSize());
            dto.setContentType(f.getContentType());
            result.add(dto);
        }

        return result;
    }

    /**
     * fileId 기준 삭제 (Delete)
     */
    @Override
    public Map<String, String> deleteByfileId(Long fileId, String email) {

        FileAttachment file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일이 없습니다. fileId = " + fileId));

        // TODO: 나중에 필요하면 여기서 email로 권한 체크

        // 1) DB 삭제
        fileRepository.delete(file);

        // 2) 실제 파일 삭제 (실패해도 일단 무시)
        if (file.getStoredPath() != null) {
            try {
                Path p = Paths.get(file.getStoredPath());
                Files.deleteIfExists(p);
            } catch (IOException e) {
                // 로깅만 하고 넘기고 싶으면 logger 쓰면 됨
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("result", "SUCCESS");
        return result;
    }

    /**
     * 파일 메타데이터 수정 (Update)
     * 지금은 예시로 originalName만 수정한다고 가정
     */
    @Override
    public FileResponseDTO updateFile(Long fileId, FileUpdateDTO dto) {

        FileAttachment file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일이 없습니다. fileId = " + fileId));

        // FileUpdateDTO 안에 뭐가 있는지에 따라 여기 수정


        FileAttachment saved = fileRepository.save(file);

        FileResponseDTO result = new FileResponseDTO();
        result.setFileId(saved.getId());
        result.setPostId(saved.getPost().getPostId());
        result.setOriginalName(saved.getOriginalName());
        result.setStoredName(saved.getStoredName());
        result.setStoredPath(saved.getStoredPath());
        result.setSize(saved.getSize());
        result.setContentType(saved.getContentType());

        return result;
    }

    /**
     * 특정 게시글의 파일 리스트 조회 (Read)
     */
    @Override
    public List<FileResponseDTO> getFilesByPostId(Long postId) {

        List<FileAttachment> files = fileRepository.findByPost_PostId(postId);

        List<FileResponseDTO> result = new ArrayList<>();

        for (FileAttachment f : files) {
            FileResponseDTO dto = new FileResponseDTO();
            dto.setFileId(f.getId());
            dto.setPostId(f.getPost().getPostId());
            dto.setOriginalName(f.getOriginalName());
            dto.setStoredName(f.getStoredName());
            dto.setStoredPath(f.getStoredPath());
            dto.setSize(f.getSize());
            dto.setContentType(f.getContentType());
            result.add(dto);
        }

        return result;
    }

    /**
     * 안 쓰는 메서드들 – 일단 비워둠
     * 필요해지면 그때 구현하면 됨.
     */
    @Override
    public void deletefile(MultipartFile file) {
        // 사용 안 함
    }

    @Override
    public FileResponseDTO FileUpdate(String fileName, FileUpdateDTO dto) {
        // 사용 안 함 (지금은 updateFile(Long, FileUpdateDTO)만 사용)
        return null;
    }

    @Override
    public FileResponseDTO storeFile(MultipartFile file) {
        // 사용 안 함 (지금은 saveFiles로 통일)
        return null;
    }

    @Override
    public List<FileResponseDTO> getAllFiles() {
        return fileRepository.findAll()
                .stream()
                .map(FileResponseDTO::fromEntity)
                .toList();
    }
}
