package com.univercity.unlimited.greenUniverCity.service;

import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
