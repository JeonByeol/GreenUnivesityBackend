package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface ReviewService {
    List<ReviewDTO> findAllReview();

    ResponseEntity<String> addReview(ReviewDTO reviewDTO);
}
