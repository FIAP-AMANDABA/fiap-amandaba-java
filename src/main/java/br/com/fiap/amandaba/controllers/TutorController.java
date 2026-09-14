package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    private final AuthService authService;

    public TutorController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/dadosTutor/{userId}")
    public ResponseEntity<Tutor> getDadosTutor(
            @PathVariable Long userId
    ){
        Tutor tutor = authService.getTutor(userId);
        return ResponseEntity.ok(tutor);
    }
}
