package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class FileCreateDTO {
    private Long postId;
    private String fileName;
    private String filePath;
    private MultipartFile files;
}
