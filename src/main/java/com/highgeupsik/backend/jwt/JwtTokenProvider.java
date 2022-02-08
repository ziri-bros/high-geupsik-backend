package com.highgeupsik.backend.jwt;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_TIME = 24 * 7 * 30 * 60 * 1000L; //테스트
    private static final long REFRESH_TOKEN_TIME = 24 * 7 * 30 * 60 * 1000L;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userId, String role) {
        Date now = new Date();
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim(AUTHORITIES_KEY, role)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getUserPK(token), "", getAuthorities(token));
    }

    public Long getUserPK(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        return List.of(new SimpleGrantedAuthority(parseClaims(token).get(AUTHORITIES_KEY).toString()));
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        log.info(header);
        if (StringUtils.hasText(header) && header.startsWith("Bearer")) {
            return header.substring(7);
        }
        return null;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new TokenException(UNABLE_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new TokenException(TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new TokenException(UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new TokenException(WRONG_JWT_TOKEN);
        }
    }
}
