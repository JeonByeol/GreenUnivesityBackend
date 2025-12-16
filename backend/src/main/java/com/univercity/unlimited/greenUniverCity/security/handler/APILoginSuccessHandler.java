package com.univercity.unlimited.greenUniverCity.security.handler;

import com.google.gson.Gson;
import com.univercity.unlimited.greenUniverCity.security.CustomUserDetails;
import com.univercity.unlimited.greenUniverCity.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("🎉 로그인 성공 authentication => {}", authentication);

        // Principal 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        String nickname = userDetails.getNickname();
        List<String> roles = userDetails.getRoleNames();

        log.info("로그인 사용자: email={}, nickname={}, roles={}", email, nickname, roles);

        // ------------------------------
        // 1) JWT Claims 구성
        // ------------------------------
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("nickname", nickname);
        claims.put("roles", roles);

        // ------------------------------
        // 2) JWT 토큰 생성
        // ------------------------------
        String accessToken = JWTUtil.generateToken(claims, 60 * 24);
        // 24시간 (원하면 변경 가능)

        // ------------------------------
        // 3) JSON Response 반환
        // ------------------------------
        Map<String, Object> result = Map.of(
                "ok", true,
                "email", email,
                "nickname", nickname,
                "roles", roles,
                "accessToken", accessToken
        );

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.print(new Gson().toJson(result));
        out.flush();

        log.info("🔐 JWT 생성 완료 → {}", accessToken);
    }
}
