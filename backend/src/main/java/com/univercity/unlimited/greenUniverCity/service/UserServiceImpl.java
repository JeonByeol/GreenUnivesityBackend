package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
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
    public List<UserVo> findAllUsers() {
        log.info("모든 유저를 조회하는 service 코드 실행");
        return userRepository.findAll();
    }

    @Override
    public UserVo findByUser(String id) {
        log.info("한명의 회원을 조회하는 service 생성");
        return userRepository.findById(id);
    }

    @Override
    public List<UserVo> findUsersByRole(UserRole role) {
        log.info("role에 해당 하는 부분의 데이터만 조회");
        return userRepository.findAllByRole(role);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        Optional<UserVo> userOptional = Optional.ofNullable(userRepository.getUserByEmail(userDTO.getEmail()));
//        UserVo user=userRepository.getUserByEmail(userDTO.getEmail()).get();
        if (userOptional.isEmpty()) {
            log.warn("로그인 정보가 없다:{}", userDTO.getEmail());
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        UserVo u = userOptional.get();
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
        UserVo userVo=mapper.map(dto,UserVo.class);
        String data = dto.getRole();
        log.info("1) data:{}",data);
        if(data.equals("학생")) {
//            roles.add(UserRole.STUDENT);
//           userVo.setUserRoleList(roles);
           userVo.addRole(UserRole.STUDENT);
        } else if(data.equals("교수")){
            userVo.addRole(UserRole.PROFESSOR);
        }
        log.info("2) IF 이후  :{}",userVo);
        userRepository.save(userVo);

        UserVo savedUser = userRepository.save(userVo);
        log.info("3) savedUser:{}",savedUser);

        return UserDTO.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .role(savedUser.getUserRoleList().get(0).name()) // (예시) 첫 번째 역할 반환
                .build();
    }

}
