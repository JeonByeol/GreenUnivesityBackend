package com.univercity.unlimited.greenUniverCity.function.member.user.service;

import com.univercity.unlimited.greenUniverCity.config.PasswordEncoderConfig;
import com.univercity.unlimited.greenUniverCity.function.academic.enrollment.exception.UserNotFoundException;
import com.univercity.unlimited.greenUniverCity.function.community.review.exception.InvalidRoleException;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserDTO> dto=new ArrayList<>();
        for (User i:userRepository.findAll()){
            log.info("1)유저에 대한 칼럼 내역은 어떻게 작동하는지:{}",i);
            UserDTO r= mapper.map(i,UserDTO.class);
            dto.add(r);
        }
        log.info("모든 유저를 조회하는 service 코드 실행:{}",dto);
        return dto;
    }

//    @Override
//    public User findByUser(String id) {
//        log.info("한명의 회원을 조회하는 service 생성");
//        return userRepository.findById(id);
//    }

    @Override
    public List<User> findUsersByRole(UserRole role) {
        log.info("role에 해당 하는 부분의 데이터만 조회");
        return userRepository.findAllByRole(role);
    }


    @Override
    public UserDTO login(UserDTO userDTO) {
        Optional<User> userOptional =
                Optional.ofNullable(userRepository.getUserByEmail(userDTO.getEmail()));

        if (userOptional.isEmpty()) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        User u = userOptional.get();

        // 🔥 암호화된 비밀번호 검증
        if (!passwordEncoder.matches(userDTO.getPassword(), u.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        log.info("로그인 성공: {}", u.getEmail());

        return UserDTO.builder()
                .userId(u.getUserId())
                .email(u.getEmail())
                .nickname(u.getNickname())
                .roleNames(u.getUserRoleList())
                .build();
    }


    @Override
    public UserDTO register(UserDTO dto) {
        log.info("register dto => {}", dto);

        // 1) User 엔티티 생성
        User user = mapper.map(dto, User.class);

        // 2) 역할 검증
        List<UserRole> names = dto.getRoleNames();
        if (names == null || names.isEmpty()) {
            throw new RuntimeException("역할(roleNames)이 없습니다.");
        }

        // 3) String → Enum 변환
        List<UserRole> roles = new ArrayList<>();
        for (UserRole r : names) {
            roles.add(r);   // "STUDENT" → UserRole.STUDENT
        }

        // 4) user 엔티티에 세팅
        user.setUserRoleList(roles);

        // 5) 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 6) 저장
        User saved = userRepository.save(user);

        log.info("saved user => {}", saved);

        // 7) DTO 반환
        return UserDTO.builder()
                .userId(saved.getUserId())
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .roleNames(saved.getUserRoleList())
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

        if (!user.getUserRoleList().contains(UserRole.PROFESSOR)) {
            throw new InvalidRoleException(
                    "교수 권한이 없습니다. userId: " + userId + ", 현재 역할: " + user.getUserRoleList().get(0)
            );
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("사용자를 찾을 수 없습니다. email:"+email));
    }

//    @Override
//    @Transactional
//    public UserDTO grade(Long userId) { ***없앨예정***
////        Optional<UserVo> userEntities = userRepository.findUserWithGradesById(userId);
////        List<UserDTO> dto=new ArrayList<>();
////        log.info("1)여기가 문제인지 dto:{}",dto);
////        for(UserVo i:userEntities){
////            log.info("2)아니면 여기가 문제인지 i:{}",i);
////            UserDTO r=mapper.map(i,UserDTO.class);
////            log.info("3)그것도 아니면 여기가 문제인지 r:{}",r);
////            dto.add(r);
////        }
////        return dto;

//        // 1. 유저를 조회하고, 없으면 예외(Exception)를 발생시킴
//        User user = userRepository.findUserWithGradesById(userId)
//                .orElseThrow(() -> {
//                    log.warn("ID: {}에 해당하는 유저를 찾을 수 없음", userId);
//                    return new RuntimeException("User not found with id: " + userId);
//                });
//        log.info("2) 유저 찾음: {}", user.getNickname());
//        // 2. ModelMapper로 변환 ()
//        UserDTO dto = mapper.map(user, UserDTO.class);
//        log.info("3) DTO로 변환 완료");
//        // 3. List가 아닌 DTO 객체 1개를 반환
//        return dto;
//    }

}
