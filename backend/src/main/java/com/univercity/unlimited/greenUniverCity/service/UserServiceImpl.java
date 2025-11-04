package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Override
    public List<UserVo> findAllUsers() {
        log.info("모든 유저를 조회하는 service 코드 실행");
        return userRepository.findAll();
    }

    @Override
    public List<UserVo> findUsersByRole(UserRole role) {
        log.info("role에 해당 하는 부분의 데이터만 조회");
        return userRepository.findAllByRole(role);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        Optional<UserVo> userOptional= Optional.ofNullable(userRepository.getUserByEmail(userDTO.getEmail()));
//        UserVo user=userRepository.getUserByEmail(userDTO.getEmail()).get();
        if(userOptional.isEmpty()){
            log.warn("로그인 정보가 없다:{}",userDTO.getEmail());
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        UserVo u=userOptional.get();
//        if(userDTO.getPassword().equals(u.getPassword())){
//            log.warn("로그인 실패: 비밀번호 불일치");
//            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
//        }
        // 추후에 passwordEncode와 같은 비밀번호 암호화와 같은 기능을 생성하고 이 암호화 비밀번호를 검증 시키기 위한 코드 추후 사용 예정

        log.info("로그인 성공: {}", u.getEmail());
        return UserDTO.builder()
                .uno(u.getUno())
                .id(u.getId())
                .email(u.getEmail())
                .nickname(u.getNickname())
                .build();
    }
}
