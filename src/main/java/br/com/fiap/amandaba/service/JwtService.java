package br.com.fiap.amandaba.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final String issuer;
    private final String audience;
    private final long validityMilliseconds;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.issuer:amandaba-java}") String issuer,
                      @Value("${app.jwt.audience:amandaba-apis}") String audience,
                      @Value("${app.jwt.validity-ms:3600000}") long validityMilliseconds) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32)
            throw new IllegalArgumentException("JWT_SECRET deve ter pelo menos 32 bytes UTF-8");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.audience = audience;
        this.validityMilliseconds = validityMilliseconds;
    }

    public String generateToken(String email, Long idUsuario, String perfil) {
        Date now = new Date();
        return Jwts.builder()
                .subject(email)
                .issuer(issuer)
                .audience().add(audience).and()
                .claim("userId", idUsuario)
                .claim("perfil", perfil)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validityMilliseconds))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseAndValidate(String token) {
        Claims claims = Jwts.parser().verifyWith(key).requireIssuer(issuer)
                .build().parseSignedClaims(token).getPayload();
        if (claims.getAudience() == null || !claims.getAudience().contains(audience))
            throw new IllegalArgumentException("JWT com audiencia invalida");
        return claims;
    }
}
