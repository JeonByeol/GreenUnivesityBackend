package com.univercity.unlimited.greenUniverCity.function.member.user.service;

import com.univercity.unlimited.greenUniverCity.config.PasswordEncoderConfig;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.member.department.entity.Department;
import com.univercity.unlimited.greenUniverCity.function.member.department.service.DepartmentService;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserLoginDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserResponseDTO;
import com.univercity.unlimited.greenUniverCity.util.MapperUtil;
import com.univercity.unlimited.greenUniverCity.util.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final DepartmentService departmentService;

    private final PasswordEncoder encoder;

    @Override
    public List<UserResponseDTO> findAllUsers() {
        List<UserResponseDTO> dto = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            log.info("1)유저에 대한 칼럼 내역은 어떻게 작동하는지:{}", user);

            // isDelete 체크 제거 - 모든 사용자 반환
            // if(user.isDelete())
            //     continue;

            UserResponseDTO responseDTO = new UserResponseDTO();

            MapperUtil.updateFrom(user, responseDTO, new ArrayList<>());
            responseDTO.setDeptName(user.getDepartment().getDeptName());
            responseDTO.setRole(user.getUserRole().toString());
            responseDTO.setDelete(  user.isDelete()); // isDelete 상태도 DTO에 포함

            dto.add(responseDTO);
        }
        log.info("모든 유저를 조회하는 service 코드 실행:{}", dto);
        return dto;
    }

    @Override
    public List<User> findUsersByRole(UserRole role) {
        log.info("role에 해당 하는 부분의 데이터만 조회");
        return userRepository.findAllByRole(role);
    }

    @Override
    public UserResponseDTO login(UserLoginDTO userDTO) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserByEmail(userDTO.getEmail()));
//        UserVo user=userRepository.getUserByEmail(userDTO.getEmail()).get();
        if (userOptional.isEmpty()) {
            log.warn("로그인 정보가 없다:{}", userDTO.getEmail());
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 검증
        // 검증 시에, 첫번째가 평문, 두번째가 암호화문입니다.
        if (!encoder.matches(userDTO.getPassword(),userOptional.get().getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        User user = userOptional.get();

        // 추후에 passwordEncode와 같은 비밀번호 암호화와 같은 기능을 생성하고 이 암호화 비밀번호를 검증 시키기 위한 코드 추후 사용 예정
        log.info("로그인 성공: {}", user.getEmail());
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getUserRole().name())
                .studentNumber(user.getStudentNumber())
                .deptName(
                        user.getDepartment() != null
                                ? user.getDepartment().getDeptName()
                                : null
                )
                .build();
    }

    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        log.info("service user, register dto=>{}", dto);

        User user = mapper.map(dto, User.class);

        // 1. 역할 매핑
        UserRole role;
        switch (dto.getRole()) {
            case "STUDENT" -> role = UserRole.STUDENT;
            case "PROFESSOR" -> role = UserRole.PROFESSOR;
            case "ADMIN" -> role = UserRole.ADMIN;
            default -> throw new IllegalArgumentException("알 수 없는 역할: " + dto.getRole());
        }
        user.setUserRole(role);

        log.info("dto.deptName {}", dto.getDeptName());
        Department department = departmentService.findByName(dto.getDeptName());
        log.info("서비스에서 가져온 department {}", department);

        user.setDepartment(department);
        user.setPassword(encoder.encode(user.getPassword()));

        // 3. 저장
        User savedUser = userRepository.save(user);
        log.info("savedUser: {}", savedUser);

        // 4. 응답 DTO
        return UserResponseDTO.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .role(savedUser.getUserRole().name())
                .studentNumber(savedUser.getStudentNumber())
//                .password(savedUser.getPassword()))
                .deptName(
                        savedUser.getDepartment() != null
                                ? savedUser.getDepartment().getDeptName()
                                : null
                )
                .build();
    }


    @Override
    public User getUserById(Long userId) {
        User user=userRepository.findByUserId(userId);

        if(user == null){
            throw new UserNotFoundException("사용자를 찾을 수 없습니다. id:"+ userId);
        }

        return user;
    }

    @Override
    public User getProfessorById(Long userId) {
        User user=userRepository.findProfessorById(userId)
                .orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다. id:"+ userId));

        if (!user.getUserRole().equals(UserRole.PROFESSOR)) {
            throw new InvalidRoleException(
                    "교수 권한이 없습니다. userId: " + userId + ", 현재 역할: " + user.getUserRole()
            );
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다. email:"+email));
    }

    @Override
    public UserResponseDTO changePassword(String password, String email) {
        // encoder.encode(savedUser.getPassword())
        User user = userRepository.getUserByEmail(email);
        user.setPassword(encoder.encode(password));
        User savedUser = userRepository.save(user);

        // 4. 응답 DTO
        return UserResponseDTO.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .role(savedUser.getUserRole().name())
                .studentNumber(savedUser.getStudentNumber())
                .password(encoder.encode(savedUser.getPassword()))
                .deptName(
                        savedUser.getDepartment() != null
                                ? savedUser.getDepartment().getDeptName()
                                : null
                )
                .build();
    }

    @Override
    public Map<String, String> deleteByUserEmail(Long userId,String email) {
        User user = userRepository.findByUserId(userId);

        if(user.isDelete())
            return Map.of("failure","이미 비활성화 상태 입니다.");

        user.setDelete(true);
        userRepository.save(user);
        return Map.of("success","비활성화에 성공하였습니다.");
    }
}
