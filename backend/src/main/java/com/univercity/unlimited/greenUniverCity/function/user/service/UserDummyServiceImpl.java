package com.univercity.unlimited.greenUniverCity.function.user.service;

import com.univercity.unlimited.greenUniverCity.function.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserLoginDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.function.user.dto.UserUpdateDTO;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserDummy;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.user.repository.UserDummyRepository;
import com.univercity.unlimited.greenUniverCity.function.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserDummyServiceImpl implements UserDummyService{

    private final UserDummyRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * U-A) User 엔티티를 (Response) DTO로 변환
     * - 각각의 crud 기능에 사용되는 서비스 구현부에 사용하기 위하여 함수로 생성하였음
     */
    private UserResponseDTO convertToResponseDTO(UserDummy user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name()) // 단순히 role.name() 호출
                .build();
    }

    /**
     * U-Order_By
     * -사용자가 로그인,회원가입을 시도할때 검증을 해주는 함수
     */
    private void exFunctionToMatch(UserDummy user,String matchEmail,String action){
        log.info(" 사용자 -:{} - 이메일 검증 -:{}",action,matchEmail);

        //로그인 시도시 이메일로 사용자 찾기
        if(user.getEmail()==null){
            throw new RuntimeException(
                    String.format(
                            "보안 검사 시도 식별코드-: U-security-1(사용자 %s) " +
                                    "데이터 오류: 이메일 또는 비밀번호가 일치하지 않습니다 email:%s," +
                                    action,user.getEmail())
            );
        }

        //로그인 시도시 비밀번호로 사용자 찾기
        if(!passwordEncoder.matches(user.getPassword(),matchEmail)){
            throw new RuntimeException(
                    String.format(
                            "보안 검사 시도 식별코드-: U-security-2(사용자 %s) " +
                                    "데이터 오류: 이메일 또는 비밀번호가 일치하지 않습니다 getPassword:%s," +
                                    action,user.getPassword())
            );
        }

    }

    //U-1
    @Override
    public List<UserResponseDTO> findAllUsers() {
        log.info("2) 전체 유저 조회 시작");

        List<UserResponseDTO> result = userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();

        log.info("3) 전체 유저 조회 완료: {}명", result.size());

        return result;
    }

    //U-2
    @Override
    public List<UserResponseDTO> findUsersByRole(UserRole role) {
        log.info("2) 역할별 유저 조회 시작 -:{}", role);

        return userRepository.findByRole(role).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    //U-3
    @Override
    public UserResponseDTO login(UserLoginDTO loginDTO) {
        log.info("로그인 시도: {}", loginDTO.getEmail());

        // 1. 이메일로 사용자 찾기
        UserDummy user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 이메일: {}", loginDTO.getEmail());
                    return new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
                });

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("비밀번호 불일치: {}", loginDTO.getEmail());
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        log.info("로그인 성공: {}", user.getEmail());

        return convertToResponseDTO(user);
    }

    //U-4
    @Transactional
    @Override
    public UserResponseDTO register(UserRegisterDTO registerDTO) {
        log.info("회원가입 시도: {}", registerDTO.getEmail());

        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            log.warn("이미 존재하는 이메일: {}", registerDTO.getEmail());
            throw new RuntimeException("이미 존재하는 이메일입니다: " + registerDTO.getEmail());
        }

        // 2. 닉네임 중복 체크
        if (userRepository.existsByNickname(registerDTO.getNickname())) {
            log.warn("이미 존재하는 닉네임: {}", registerDTO.getNickname());
            throw new RuntimeException("이미 존재하는 닉네임입니다: " + registerDTO.getNickname());
        }

        // 3. 역할 결정 (훨씬 간단해짐!)
        UserRole role;
        String roleStr = registerDTO.getRole();

        if ("학생".equals(roleStr)) {
            role = UserRole.STUDENT;
        } else if ("교수".equals(roleStr)) {
            role = UserRole.PROFESSOR;
        } else if ("관리자".equals(roleStr)) {
            role = UserRole.ADMIN;
        } else {
            log.warn("잘못된 역할: {}", roleStr);
            throw new RuntimeException("잘못된 역할입니다: " + roleStr + " (학생, 교수, 관리자 중 선택)");
        }

        // 4. User 엔티티 생성 (깔끔해짐!)
        UserDummy user = UserDummy.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .nickname(registerDTO.getNickname())
                .role(role)
                .build();

        // 5. 저장
        UserDummy savedUser = userRepository.save(user);
        log.info("회원가입 완료: userId={}, email={}, role={}",
                savedUser.getUserId(), savedUser.getEmail(), savedUser.getRole());

        return convertToResponseDTO(savedUser);
    }

    //U-4
    @Override
    public UserDummy getUserById(Long userId) {
        log.info("유저 조회 by ID: {}", userId);

        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. id: " + userId));
    }

    //U-5
    @Override
    public UserDummy getProfessorById(Long userId) {
        log.info("교수 권한 유저 조회: {}", userId);

        UserDummy user = userRepository.findProfessorById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. id: " + userId));

        // ✅ 간단한 권한 체크
        if (!user.isProfessor()) {
            throw new InvalidRoleException(
                    "교수 권한이 없습니다. userId: " + userId + ", 현재 역할: " + user.getRole()
            );
        }

        return user;
    }

    //U-6
    @Override
    public UserDummy getUserByEmail(String email) {
        log.info("유저 조회 by Email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. email: " + email));
    }

    //U-7
    @Transactional
    @Override
    public UserResponseDTO updateUser(Long userId, UserUpdateDTO updateDTO) {
        log.info("유저 정보 수정: userId={}", userId);

        UserDummy user = getUserById(userId);

        // 비밀번호 변경
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(updateDTO.getPassword());
            user.changePassword(encodedPassword);
            log.info("비밀번호 변경 완료: userId={}", userId);
        }

        // 닉네임 변경
        if (updateDTO.getNickname() != null && !updateDTO.getNickname().isBlank()) {
            if (userRepository.existsByNickname(updateDTO.getNickname())) {
                throw new RuntimeException("이미 존재하는 닉네임입니다: " + updateDTO.getNickname());
            }
            user.changeNickname(updateDTO.getNickname());
            log.info("닉네임 변경 완료: userId={}, newNickname={}", userId, updateDTO.getNickname());
        }

        return convertToResponseDTO(user);
    }



}

