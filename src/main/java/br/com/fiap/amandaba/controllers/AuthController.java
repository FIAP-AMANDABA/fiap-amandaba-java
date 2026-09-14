package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.*;
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
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request) {

        System.out.println("REGISTER ENDPOINT HIT");
        User user = authService.register(request);
        Tutor tutorCriado = authService.getTutor(user.getId());
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDataNascimento(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getTelefone(),
                user.getDataNascimento(),
                user.getStatusUsuario(),
                tutorCriado.getIdTutor()
        );
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest request) {

        String token = authService.login(request);
        User user = authService.getUser(request.email());
        Tutor tutor = authService.getTutor(user.getId());
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDataNascimento(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getTelefone(),
                user.getDataNascimento(),
                user.getStatusUsuario(),
                tutor.getIdTutor()
        );
        return ResponseEntity.ok(userResponse
        );
    }
}