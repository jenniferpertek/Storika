package at.pertek.storika.user_service.services;

import at.pertek.storika.user_service.entities.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class JwtService {
  private final String secret = "very-secret-key";

  public String generateToken(AppUser user) {
    return Jwts.builder()
        .setSubject(user.getId().toString())
        .claim("username", user.getUsername())
        .claim("role", user.getRole())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
        .signWith(SignatureAlgorithm.HS256, secret.getBytes())
        .compact();
  }

  public UUID parseToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secret.getBytes())
        .parseClaimsJws(token)
        .getBody();
    return UUID.fromString(claims.getSubject());
  }
}

