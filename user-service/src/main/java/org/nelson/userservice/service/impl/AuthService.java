package org.nelson.userservice.service.impl;

import lombok.AllArgsConstructor;
import org.nelson.userservice.dto.AuthResponse;
import org.nelson.userservice.dto.LoginRequest;
import org.nelson.userservice.dto.RegistrationRequest;
import org.nelson.userservice.entity.Token;
import org.nelson.userservice.entity.User;
import org.nelson.userservice.repository.TokenRepository;
import org.nelson.userservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthResponse register(RegistrationRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthResponse(jwt);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        revokeAllTokenOfUser(user);
        saveUserToken(token, user);

        return new AuthResponse(token);
    }

    private void revokeAllTokenOfUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());
        if (!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(t -> t.setLoggedOut(true));
        }
        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);

    }
}
