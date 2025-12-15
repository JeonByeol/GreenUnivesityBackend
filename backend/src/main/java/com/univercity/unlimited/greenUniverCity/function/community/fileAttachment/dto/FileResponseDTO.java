package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.entity.FileAttachment;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDTO {

    private Long id;
    private Long postId;
    private String originalName;
    private String storedName;
    private String storedPath;
    private Long size;
    private String contentType;

    public static FileResponseDTO fromEntity(FileAttachment entity) {
        return FileResponseDTO.builder()
                .id(entity.getId())
                .postId(entity.getPost() != null ? entity.getPost().getPostId() : null)
                .originalName(entity.getOriginalName())
                .storedName(entity.getStoredName())
                .storedPath(entity.getStoredPath())
                .size(entity.getSize())
                .contentType(entity.getContentType())
                .build();
    }

    public void setFileId(Long id) {
    }
}
