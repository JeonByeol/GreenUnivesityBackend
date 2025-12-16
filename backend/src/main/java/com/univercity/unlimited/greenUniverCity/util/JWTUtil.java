package com.univercity.unlimited.greenUniverCity.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JWTUtil {

    // (데모용) 정확히 32바이트: 운영에서는 환경변수/시크릿으로 주입 권장
    private static final String RAW_SECRET_32B = "12345678901234567890123456789012"; // 32 bytes

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(RAW_SECRET_32B.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey key = getKey();

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(valueMap)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + min * 60_000L))
                .signWith(key, SignatureAlgorithm.HS256)  // ★ 반드시 서명!
                .compact();
    }

    public static Map<String, Object> validateToken(String token) {
        try {
            SecretKey key = getKey();
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new CustomJWTException("MalFormed");
        } catch (ExpiredJwtException e) {
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException e) {
            throw new CustomJWTException("Invalid");
        } catch (JwtException e) {
            throw new CustomJWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }
    }
}
