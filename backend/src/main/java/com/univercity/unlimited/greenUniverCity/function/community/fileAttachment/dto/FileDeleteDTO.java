package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDeleteDTO {
    private String fileName;
    private String filePath;
}
