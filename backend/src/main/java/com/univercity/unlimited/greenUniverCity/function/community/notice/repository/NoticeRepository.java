package com.univercity.unlimited.greenUniverCity.function.community.notice.repository;

import com.univercity.unlimited.greenUniverCity.function.community.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository <Notice,Long>{
    @Query("select n from Notice n left join fetch n.user order by n.createdAt desc")
    List<Notice> findAllWithUser();
}
