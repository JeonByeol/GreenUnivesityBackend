package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.service;

import com.univercity.unlimited.greenUniverCity.function.community.board.dto.BoardResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileUpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface FileService {
    // F-1 조회
    List<FileResponseDTO> getAllFiles();
    // F-1-1 단건조회
    FileResponseDTO findId(Long fileId);
    // F-2 생성
    List<FileResponseDTO> createdfile(Long fileId, Long postId);
    // F-3 추가
    FileResponseDTO updateFile(Long fileId, FileUpdateDTO dto);
    // F-4 삭제
    void deleteFile(MultipartFile file, Long fileId);

    //삭제
    void deleteSearch(Long boardId);

    //삭제
//    void deleteFile(MultipartFile file);

    //추가
//    FileUpdateDTO UpdateFile(FileUpdateDTO dto);


//    List<FileResponseDTO> saveFiles(List<MultipartFile> files, Long postId);

    }



