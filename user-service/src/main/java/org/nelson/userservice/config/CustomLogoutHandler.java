package org.nelson.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.nelson.userservice.entity.Token;
import org.nelson.userservice.repository.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        String token = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(token).orElse(null);
        assert storedToken != null;
        storedToken.setLoggedOut(true);
        tokenRepository.save(storedToken);
    }
}
