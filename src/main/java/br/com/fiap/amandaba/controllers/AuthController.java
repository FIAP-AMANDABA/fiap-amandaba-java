package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.request.*;
import br.com.fiap.amandaba.dto.response.*;
import br.com.fiap.amandaba.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth=auth; }

    @PostMapping("/register/tutor")
    public ResponseEntity<TutorResponse> registerTutor(@Valid @RequestBody RegisterRequestTutor request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.registerTutor(request));
    }
    @PostMapping("/register/vet")
    public ResponseEntity<VetResponse> registerVet(@Valid @RequestBody RegisterRequestVeterinario request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auth.registerVeterinario(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(auth.login(request));
    }
}
