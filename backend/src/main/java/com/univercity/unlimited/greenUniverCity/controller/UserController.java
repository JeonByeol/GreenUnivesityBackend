package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    //    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/all") //전체 데이터 조회
    public List<UserDTO> postmanTestUser() {
        log.info("Controller: /api/user/all 호출");
        return userService.findAllUsers();
    }
    @GetMapping("/one/{id}")
    public ResponseEntity<UserVo> postmanTestOne(@PathVariable("id")  String id){
        log.info("Controller 하나 호출 id:{}",id);
        UserVo r=userService.findByUser(id);
        log.info("찾는중 테스트:{}",r);
        return ResponseEntity.ok(r);
    }
    @GetMapping("/role/{roleName}") //role 데이터 조회
    public List<UserVo> postmanTestRole(@PathVariable("roleName") UserRole role) {
        log.info("Controller: /api/user/role/{} 호출", role);
        return userService.findUsersByRole(role);
    }

    @PostMapping("/login") //로그인
    public UserDTO login(@RequestBody UserDTO dto) {
        log.info("Controller:/api/user/login:{} 확인", dto);
        return userService.login(dto);
    }

    @PostMapping("/register") //회원가입
//    private  ResponseEntity<String > rr(@RequestBody UserDTO dto) {
    public UserDTO register(@RequestBody UserDTO dto) {
        log.info("Controller:/api/user/register:{} 확인", dto);
//        vo.addRole(UserRole.STUDENT);
//         userService.register(dto);
//         return ResponseEntity.ok(dto.getNickname());
        return  userService.register(dto);
    }

//    @GetMapping("/grade/{gradeValue}")
//    public ResponseEntity<List<UserDTO>> getUsersByGrade(@PathVariable("gradeValue") Long userId) {
//        log.info("Controller: '{}' 성적을 받은 유저 조회", gradeValue);
//
//        // 이전에 작성하신 service의 'grade(String gradeValue)' 메서드를 호출합니다.
//        List<UserDTO> userList = userService.grade(gradeValue);
//
//        return ResponseEntity.ok(userList);
//    }
    @GetMapping("/grades/id/{userId}")
    public ResponseEntity<UserDTO> getUserWithGrades(@PathVariable("userId") Long userId) {
         log.info("Controller: ID {} 유저의 성적 정보 조회", userId);

        // 이전에 수정한 (Optional을 사용하고 UserDTO 1개를 반환하는)
        // userService.grade(Long userId) 메서드를 호출합니다.
        UserDTO userDTO = userService.grade(userId);

        return ResponseEntity.ok(userDTO);
    }

}
