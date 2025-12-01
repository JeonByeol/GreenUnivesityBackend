package com.univercity.unlimited.greenUniverCity.function.community.notice.repository;

import com.univercity.unlimited.greenUniverCity.function.community.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository <Notice,Long>{
    //uservo의 타입
}
