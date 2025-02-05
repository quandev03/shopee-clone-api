package com.example.banhangapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private static final String SECRET_512 = "3CD8/0T1pPT4gFJ3Hu8JP8vob7vKEnBLr7q/E/H7bcS9U8GoAHmvF7FN30Fte9/IClz95JZkr8y8Pxq21D+ETw==";

    // Thời gian sống của Access Token (5 phút)
    private final long accessTokenExpiration = 60 * 60 * 1000;

    // Thời gian sống của Refresh Token (7 ngày)
    private final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000;

    private final Key key;

    public JwtService() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_512);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // ===================== Tạo Access Token =====================

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS512)  // Sử dụng HS512
                .compact();
    }

    // ===================== Tạo Refresh Token =====================

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
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
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===================== Kiểm tra hạn và validate Token =====================

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
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
            return false;
        }
    }

    public String getUsernameFromExpiredToken(String token, Key key) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}