package com.hobby.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

@Service
public class JwtProvider {

    @Value("${jwt.expiration.time}")
    private long jwtTokenExpirationSecs;

    @Autowired
    private KeyStoreManager keyStoreManager;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .setExpiration(from(Instant.now().plusSeconds(jwtTokenExpirationSecs)))
                .signWith(getPrivatekey())
                .compact();
    }

    public String generateTokenWithEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivatekey())
                .setExpiration(from(Instant.now().plusSeconds(jwtTokenExpirationSecs)))
                .compact();
    }

    private PrivateKey getPrivatekey() {
        return keyStoreManager.getPrivateKey("codie", "Lakhan@12");
    }

    public boolean validateToken(String jwt) {
        parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private PublicKey getPublickey() {
        return keyStoreManager.getPublicKey("codie");
    }

    public long getJwtTokenExpirationSecs() {
        return jwtTokenExpirationSecs;
    }
}
