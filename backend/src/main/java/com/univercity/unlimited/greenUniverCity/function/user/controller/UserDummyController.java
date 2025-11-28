package com.univercity.unlimited.greenUniverCity.function.user.controller;

import com.univercity.unlimited.greenUniverCity.function.user.dto.UserLoginDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserDummy;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.user.service.UserDummyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dummy")
public class UserDummyController {
    private final UserDummyService userService;

    // ===== 조회 API =====


    //U-1
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("[GET] /api/user/all - 1) 전체 사용자 조회 요청 ");

        List<UserResponseDTO> users = userService.findAllUsers();

        return ResponseEntity.ok(users);
    }

    //U-2
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @PathVariable("roleName") UserRole role) {
        log.info("[GET] /api/user/role/{} - 1) 역할별 사용자 조회 요청", role);

        List<UserResponseDTO> users = userService.findUsersByRole(role);

        return ResponseEntity.ok(users);
    }

    //U-3 ** 이대로 사용할지 검토 필요 **
    @GetMapping("/one/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        log.info("[GET] /api/user/one/{} - 사용자 조회", id);

        UserDummy user = userService.getUserById(id);

        // Entity를 직접 반환하지 않고 DTO로 변환
        UserResponseDTO response = UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(response);
    }

    // ===== 인증 API =====


    //U-4
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO loginDTO) {

        log.info("[POST] /api/user/login - 로그인 시도: {}", loginDTO.getEmail());

        try {
            UserResponseDTO response = userService.login(loginDTO);
            log.info("로그인 성공: {}", response.getEmail());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("[POST] /api/user/register - 회원가입 시도: {}", registerDTO.getEmail());

        try {
            UserResponseDTO response = userService.register(registerDTO);
            log.info("회원가입 성공: {}", response.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // ===== 수정 API =====

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        log.info("[PATCH] /api/user/{} - 사용자 정보 수정", id);

        try {
            UserResponseDTO response = userService.updateUser(id, updateDTO);
            log.info("정보 수정 성공: userId={}", id);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("정보 수정 실패: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    // ===== 헬퍼 메서드 =====

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}

