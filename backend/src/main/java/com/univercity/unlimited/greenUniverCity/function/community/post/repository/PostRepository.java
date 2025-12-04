package com.univercity.unlimited.greenUniverCity.function.community.post.repository;

import com.univercity.unlimited.greenUniverCity.function.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAll();

    List<Post> findByTitleContainingIgnoreCase(String keyword);


}
