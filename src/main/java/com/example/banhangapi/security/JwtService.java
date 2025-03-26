package com.example.banhangapi.security;

import com.example.banhangapi.config.AppConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {

    // Thời gian sống của Access Token (1 giờ)
    private final long accessTokenExpiration = 60 * 60 * 1000;

    // Thời gian sống của Refresh Token (7 ngày)
    private final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000;

    private final Key key;

    public JwtService() {
        byte[] keyBytes = Decoders.BASE64.decode("3CD8/0T1pPT4gFJ3Hu8JP8vob7vKEnBLr7q/E/H7bcS9U8GoAHmvF7FN30Fte9/IClz95JZkr8y8Pxq21D+ETw==");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // ===================== Tạo Access Token =====================
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(accessTokenExpiration)))
                .signWith(key, SignatureAlgorithm.HS512)  // Sử dụng HS512
                .compact();
    }

    // ===================== Tạo Refresh Token =====================
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(refreshTokenExpiration)))
                .signWith(key, SignatureAlgorithm.HS512) // Sử dụng HS512
                .compact();
    }

    // ===================== Trích xuất thông tin từ Token =====================
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimResolver.apply(claims);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Error extracting claims from token: " + e.getMessage(), e);
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            System.out.println("Extracting claims from token: " + token);  // Kiểm tra giá trị của token
            return Jwts.parserBuilder()
                    .setSigningKey(key)  // Kiểm tra xem key có khớp không
                    .build()
                    .parseClaimsJws(token)  // Nếu token không hợp lệ, sẽ ném lỗi ở đây
                    .getBody();
        } catch (JwtException e) {
            // In ra thông tin chi tiết về lỗi để debug
            System.out.println("Error occurred while extracting claims: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token or signature verification failed", e);
        }
    }

    // ===================== Kiểm tra hạn và validate Token =====================
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateFreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // Nếu có lỗi (token không hợp lệ hoặc hết hạn), trả về false
        }
    }

    // ===================== Lấy thông tin từ token hết hạn =====================
    public String getUsernameFromExpiredToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().getSubject(); // Nếu token hết hạn, trả về thông tin người dùng từ claims
        } catch (JwtException e) {
            return null; // Nếu có lỗi khác, trả về null
        }
    }
}