package com.bidsphere.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
 private final SecretKey key;
 public JwtService(@Value("${app.jwt-secret}") String secret) {
   if(secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalArgumentException("JWT_SECRET must be at least 32 bytes");
   key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
 }
 public String create(String email, String role) {
   long now=System.currentTimeMillis();
   return Jwts.builder().subject(email).claim("role",role).issuedAt(new Date(now)).expiration(new Date(now+86400000)).signWith(key).compact();
 }
 public Claims parse(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
