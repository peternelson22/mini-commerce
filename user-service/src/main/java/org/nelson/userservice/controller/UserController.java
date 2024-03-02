package org.nelson.userservice.controller;

import lombok.AllArgsConstructor;
import org.nelson.userservice.dto.AuthResponse;
import org.nelson.userservice.dto.LoginRequest;
import org.nelson.userservice.dto.RegistrationRequest;
import org.nelson.userservice.service.impl.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final AuthService authService;

    @PostMapping("/register")
    private ResponseEntity<AuthResponse> register(@RequestBody RegistrationRequest request){
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return new ResponseEntity<>(authService.login(request), HttpStatus.CREATED);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token){
        authService.validateToken(token);
        return new ResponseEntity<>("Token is Valid", HttpStatus.OK);
    }
}
