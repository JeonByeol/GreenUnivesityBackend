package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class FileResponseDTO {

    private Long fileId;
    private Long postId;
    private String originalName;
    private String storedName;
    private String storedPath;
    private Long size;
    private String contentType;
}
