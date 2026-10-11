package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.request.LoginRequest;
import br.com.fiap.amandaba.dto.request.RegisterRequestTutor;
import br.com.fiap.amandaba.dto.request.RegisterRequestVeterinario;
import br.com.fiap.amandaba.dto.response.TutorResponse;
import br.com.fiap.amandaba.dto.response.UserResponse;
import br.com.fiap.amandaba.dto.response.VetResponse;
import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.model.Veterinario;
import br.com.fiap.amandaba.service.AuthService;
import br.com.fiap.amandaba.service.TutorService;
import br.com.fiap.amandaba.service.VeterinarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final VeterinarioService veterinarioService;
    private final TutorService tutorService;

    public AuthController(AuthService authService, VeterinarioService veterinarioService, TutorService tutorService) {
        this.authService = authService;
        this.veterinarioService = veterinarioService;
        this.tutorService = tutorService;
    }

    @PostMapping("/register/vet")
    public ResponseEntity<VetResponse> registerVeterinario(
            @RequestBody RegisterRequestVeterinario request) {

        User user = authService.register(request);
        Veterinario veterinarioCriado = veterinarioService.veterinarioRegister(request, user.getId());

        VetResponse userResponse = new VetResponse(
                veterinarioCriado.getId(),
                veterinarioCriado.getCrmv(),
                veterinarioCriado.getVeterinarioStatus(),

                user.getId(),
                user.getEmail(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getDataCadastro(),
                user.getStatusUsuario(),

                user.getDataNascimento(),
                user.getTelefone()
                );
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/register/tutor")
    public ResponseEntity<TutorResponse> registerTutor(
            @RequestBody RegisterRequestTutor request) {

        User user = authService.register(request);
        Tutor tutorCriado = tutorService.tutorRegister(request, user.getId());

        TutorResponse userResponse = new TutorResponse(

                tutorCriado.getIdTutor(),

                user.getId(),
                user.getEmail(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getDataCadastro(),
                user.getStatusUsuario(),

                user.getDataNascimento(),
                user.getTelefone()
        );
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest request) {
        UserResponse userResponse = authService.login(request);
        return userResponse == null?
                ResponseEntity.ok(userResponse) : ResponseEntity.notFound().build();

    }
}