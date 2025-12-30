package com.univercity.unlimited.greenUniverCity.function.community.post.repository;

import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAll();

    List<Post> findByTitleContainingIgnoreCase(String keyword);

    @Modifying
    @Query("UPDATE Post p SET p.deleted = true WHERE p.postId = :postId")
    void softDelete(@Param("postId") Long postId);

}
