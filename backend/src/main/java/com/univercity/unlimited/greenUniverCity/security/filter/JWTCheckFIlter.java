package com.univercity.unlimited.greenUniverCity.security.filter;

import com.google.gson.Gson;
import com.univercity.unlimited.greenUniverCity.function.member.user.dto.UserRegisterDTO;
import com.univercity.unlimited.greenUniverCity.function.member.user.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class JWTCheckFIlter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        log.info("---------check uri:{}-------------", path);

        if (request.getMethod().equals("OPTIONS")) return true;

        // 로그인, 회원가입, 공개 URL → JWT 체크 제외
        if (path.startsWith("/api/user/")) return true;

        // 정적 파일 제외
        if (path.startsWith("/uploads/")) return true;
        if (path.startsWith("/api/products/view/")) return true;

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("-------JWTCheckFilter-----------");

        String authHeaderStr = request.getHeader("Authorization");

        // JWT 헤더가 없으면 통과(인증 없이 가능한 URL도 있으니까)
        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            log.warn("No valid Authorization header found");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // JWT 추출
            String accessToken = authHeaderStr.substring(7);

            // JWT 검증
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);
            log.info("JWT claims: {}", claims);

            // 토큰 정보
            String email = (String) claims.get("email");
            List<String> roles = (List<String>) claims.get("roles");

            // 권한 생성
            var authorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .toList();

            // principal 로 email 사용 (비밀번호 필요 없음)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            // SecurityContext 에 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Check Error-----------");
            log.error(e.getMessage(), e);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "INVALID_ACCESS_TOKEN"));
            response.getWriter().write(msg);
        }
    }
}
