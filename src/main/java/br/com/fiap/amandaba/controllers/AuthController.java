package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.AuthResponse;
import br.com.fiap.amandaba.dto.LoginRequest;
import br.com.fiap.amandaba.dto.RegisterRequest;
import br.com.fiap.amandaba.dto.TutorResponse;
import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.repository.TutorRepository;
import br.com.fiap.amandaba.service.AuthService;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        System.out.println("REGISTER ENDPOINT HIT");
        User user = authService.register(request);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        String token = authService.login(request);

        return ResponseEntity.ok(
                new AuthResponse(token)
        );
    }
}