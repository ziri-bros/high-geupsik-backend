package com.highgeupsik.backend.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; //30분
    private static final long REFRESH_TOKEN_TIME = 24 * 7 * 30 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Long userId, String role, long validTime) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim(AUTHORITIES_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createNewToken(Long userId, String role, String type) {
        if (type.equals("access"))
            return createToken(userId, role, ACCESS_TOKEN_TIME);
        return createToken(userId, role, REFRESH_TOKEN_TIME);
    }

    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getUserPK(token), "", getAuthorities(token));
    }

    public Long getUserPK(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody();
        return List.of(new SimpleGrantedAuthority(claims.get(AUTHORITIES_KEY).toString()));
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        log.info(header);
        if (StringUtils.hasText(header) && header.startsWith("Bearer"))
            return header.substring(7);
        return null;
    }

    public boolean validateToken(String jwtToken) {
        Claims claims = parseClaims(jwtToken);
        return !claims.getExpiration().before(new Date());
    }

    public Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

}
