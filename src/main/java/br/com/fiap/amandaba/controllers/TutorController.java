package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.request.AtualizarPerfilRequest;
import br.com.fiap.amandaba.dto.response.UserResponse;
import br.com.fiap.amandaba.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TutorController {
    private final PerfilService perfis;
    public TutorController(PerfilService perfis) { this.perfis=perfis; }

    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/api/tutores/me")
    public UserResponse meuPerfil(Authentication auth) { return perfis.meuPerfil(auth.getName()); }

    @PreAuthorize("hasRole('TUTOR')")
    @PutMapping("/api/tutores/me")
    public UserResponse atualizar(@Valid @RequestBody AtualizarPerfilRequest request, Authentication auth) {
        return perfis.atualizarPerfil(auth.getName(),request);
    }

    // Compatibilidade com a rota da Sprint 3; so permite consultar o proprio usuario.
    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/tutor/dadosTutor/{userId}")
    public UserResponse consultarLegado(@PathVariable Long userId, Authentication auth) {
        UserResponse perfil=perfis.meuPerfil(auth.getName());
        if (!perfil.getIdUser().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Acesso a outro usuario negado");
        return perfil;
    }
}
