package com.univercity.unlimited.greenUniverCity.function.user.service;

import com.univercity.unlimited.greenUniverCity.function.user.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.function.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.function.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    @Override
    public User findByUser(String id) {
        log.info("한명의 회원을 조회하는 service 생성");
        return userRepository.findById(id);
    }

    @Override
    public List<User> findUsersByRole(UserRole role) {
        log.info("role에 해당 하는 부분의 데이터만 조회");
        return userRepository.findAllByRole(role);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserByEmail(userDTO.getEmail()));
//        UserVo user=userRepository.getUserByEmail(userDTO.getEmail()).get();
        if (userOptional.isEmpty()) {
            log.warn("로그인 정보가 없다:{}", userDTO.getEmail());
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        User u = userOptional.get();
//        if(userDTO.getPassword().equals(u.getPassword())){
//            log.warn("로그인 실패: 비밀번호 불일치");
//            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
//        }
        // 추후에 passwordEncode와 같은 비밀번호 암호화와 같은 기능을 생성하고 이 암호화 비밀번호를 검증 시키기 위한 코드 추후 사용 예정
        log.info("로그인 성공: {}", u.getEmail());
        return UserDTO.builder()
                .userId(u.getUserId())
                .email(u.getEmail())
                .nickname(u.getNickname())
                .role(u.getUserRoleList().toString())
                .build();
    }

    @Override
    public UserDTO register(UserDTO dto) {
        log.info("service user, register dto=>{}", dto);
        User user =mapper.map(dto, User.class);
        String data = dto.getRole();
        log.info("1) data:{}",data);
        if(data.equals("학생")) {
//            roles.add(UserRole.STUDENT);
//           userVo.setUserRoleList(roles);
           user.addRole(UserRole.STUDENT);
        } else if(data.equals("교수")){
            user.addRole(UserRole.PROFESSOR);
        }
        log.info("2) IF 이후  :{}", user);
        userRepository.save(user);

        User savedUser = userRepository.save(user);
        log.info("3) savedUser:{}",savedUser);

        return UserDTO.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .role(savedUser.getUserRoleList().get(0).name()) // (예시) 첫 번째 역할 반환
                .build();
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
