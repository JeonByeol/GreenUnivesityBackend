//package com.univercity.unlimited.greenUniverCity.function.post.service;

//public class PostTraceServiceImpl implements   PostTraceService {

//    private final PostRepository postRepository;
//
//    // 내가 지금까지 추적한 Post ID들
//    private final List<Long> tracedPostIds = new ArrayList<>();
//
//    // ID로 Post를 찾으면서, 추적 리스트에 기록까지 남김
//    public Post tracePostById(Long id) {
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다. id=" + id));
//
//        tracedPostIds.add(id); // ✅ 여기서 "추적" 기록
//        return post;
//    }
//
//    // 지금까지 추적한 ID 목록 보기
//    public List<Long> getTraceIds() {
//        return List.copyOf(tracedPostIds); // 불변 리스트로 반환
//    }
//}
