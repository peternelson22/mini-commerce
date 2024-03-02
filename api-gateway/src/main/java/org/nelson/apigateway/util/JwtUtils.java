package org.nelson.apigateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;


@Service
public class JwtUtils {


    public void validateToken(final String token){
        Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        String SECRETKEY = "1d05ce61464020d6f11d2b4a5a7068524802122439a0bc1ea8eaff534e236659";
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRETKEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
