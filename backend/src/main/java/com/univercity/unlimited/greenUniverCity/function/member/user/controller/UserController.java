package com.univercity.unlimited.greenUniverCity.function.member.user.controller;

import com.univercity.unlimited.greenUniverCity.function.academic.timetable.dto.TimeTableCreateDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserLoginDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.service.UserService;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    //    private final UserRepository userRepository;
    private final UserService userService;

    // U-1) 전체 유저 조회
    @GetMapping("/all") //전체 데이터 조회
    public List<UserResponseDTO> postmanTestUser() {
        log.info("Controller: /api/user/all 호출");
        return userService.findAllUsers();
    }
    
    // U-2) 유저 아이디로 조회
    @GetMapping("/one/{id}")
    public ResponseEntity<User> postmanTestOne(@PathVariable("id")  Long id){
        log.info("Controller 하나 호출 id:{}",id);
        User r=userService.getUserById(id);
        log.info("찾는중 테스트:{}",r);
        return ResponseEntity.ok(r);
    }
    
    // U-2-2) role로 유저 조회
    @GetMapping("/role/{roleName}") //role 데이터 조회
    public List<User> postmanTestRole(@PathVariable("roleName") UserRole role) {
        log.info("Controller: /api/user/role/{} 호출", role);
        return userService.findUsersByRole(role);
    }

    // U-2-3) email로 유저 조회
    @GetMapping("/my/{email}")
    public ResponseEntity<UserResponseDTO> getMyData(
            @PathVariable("email") String email) {

        // 1) 요청 로그
        log.info("1) 본인 데이터 조회 요청 - 모든 역할-:{}", email);

        // 2) 이메일 검증 (Postman 테스트용)
        if (email == null || email.isEmpty()) {
            log.warn("X-User-Email 헤더가 없습니다. 테스트용 기본값 사용");
            email = "hannah@aaa.com";
        }

        // 3) 서비스 호출
        User user = userService.getUserByEmail(email);
        UserResponseDTO response = new UserResponseDTO();
        MapperUtil.updateFrom(user,response,new ArrayList<>());
        response.setRole(user.getUserRole().toString());
        response.setDeptName(user.getDepartment().getDeptName());


        // 4) 완료 로그
        log.info("Complete: 데이터 조회 완료 :{}", response);

        // 5) 응답 반환
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login") //로그인
    public UserResponseDTO login(@RequestBody UserLoginDTO dto) {
        log.info("Controller:/api/user/login:{} 확인", dto);
        return userService.login(dto);
    }

    @PostMapping("/create") //회원가입
//    private  ResponseEntity<String > rr(@RequestBody UserDTO dto) {
    public UserResponseDTO register(
            @RequestBody UserRegisterDTO dto,
            @RequestHeader(value="X-User-Email") String requesterEmail) {
        log.info("Controller:/api/user/register:{} 확인", dto);
        return userService.register(dto);
    }

    @PostMapping("/change")
    public UserResponseDTO changePassword(
            @RequestBody UserLoginDTO dto,
            @RequestHeader(value="X-User-Email") String requesterEmail) {
        log.info("1) 비밀번호 변경 : {}", dto);
        UserResponseDTO responseDTO = userService.changePassword(dto.getPassword(), requesterEmail);
        return responseDTO;
    }

    @DeleteMapping("/delete/{userId}")
    public Map<String, String> delete(
            @PathVariable("userId") Long userId,
            @RequestHeader(value="X-User-Email",required = false) String requesterEmail) {
        return userService.deleteByUserEmail(userId,requesterEmail);
    }
}
