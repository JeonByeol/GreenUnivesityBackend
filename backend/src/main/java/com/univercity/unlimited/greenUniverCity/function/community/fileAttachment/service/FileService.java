package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.service;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto.FileUpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface FileService {

    FileResponseDTO storeFile(MultipartFile file);

    void deletefile(MultipartFile file);

    Map<String, String> deleteByfileId(Long fileId, String email);


    FileResponseDTO FileUpdate(String fileName, FileUpdateDTO dto);

    List<FileResponseDTO> saveFiles(List<MultipartFile> files, Long postId);

    FileResponseDTO updateFile(Long fileId, FileUpdateDTO dto);

    List<FileResponseDTO> getFilesByPostId(Long postId);


    List<FileResponseDTO> getAllFiles();

    }



