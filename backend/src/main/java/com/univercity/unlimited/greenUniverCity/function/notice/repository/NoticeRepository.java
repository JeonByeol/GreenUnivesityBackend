package com.univercity.unlimited.greenUniverCity.function.notice.repository;

import com.univercity.unlimited.greenUniverCity.function.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
