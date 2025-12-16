package com.univercity.unlimited.greenUniverCity.security;

import com.univercity.unlimited.greenUniverCity.function.member.user.entity.User;
import com.univercity.unlimited.greenUniverCity.function.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("🔎 loadUserByUsername - email: {}", email);

        User user = userRepository.getWithRole(email);

        if (user == null) {
            log.warn("❌ 사용자 없음: {}", email);
            throw new UsernameNotFoundException("User not found: " + email);
        }

        log.info("✔ 사용자 조회 성공: {}", user.getEmail());
        return new CustomUserDetails(user);
    }
}
