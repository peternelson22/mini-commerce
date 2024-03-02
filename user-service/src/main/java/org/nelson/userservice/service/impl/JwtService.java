package org.nelson.userservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.nelson.userservice.entity.Token;
import org.nelson.userservice.entity.User;
import org.nelson.userservice.repository.TokenRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public void validateToken(final String token){
        Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token, UserDetails user){
        String username = extractUsername(token);
        boolean isValidToken = tokenRepository.findByToken(token)
                .map(Token::isLoggedOut).orElse(false);

        return (username.equals(user.getUsername()) && !isTokenExpired(token) && isValidToken);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        String SECRETKEY = "1d05ce61464020d6f11d2b4a5a7068524802122439a0bc1ea8eaff534e236659";
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRETKEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
