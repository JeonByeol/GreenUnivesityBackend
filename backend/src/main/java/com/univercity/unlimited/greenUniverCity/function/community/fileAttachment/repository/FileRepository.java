package com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.repository;

import com.univercity.unlimited.greenUniverCity.function.community.fileAttachment.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileAttachment, Long> {
    List<FileAttachment> findByPost_PostId(Long postId);

}
