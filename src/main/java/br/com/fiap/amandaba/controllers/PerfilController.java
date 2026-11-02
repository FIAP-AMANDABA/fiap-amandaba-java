package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.request.*;
import br.com.fiap.amandaba.dto.response.*;
import br.com.fiap.amandaba.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/me")
public class PerfilController {
    private final PerfilService perfis;
    public PerfilController(PerfilService perfis) { this.perfis=perfis; }

    @GetMapping
    public UserResponse consultar(Authentication authentication) { return perfis.meuPerfil(authentication.getName()); }

    @PutMapping
    public UserResponse atualizar(@Valid @RequestBody AtualizarPerfilRequest request,Authentication authentication) {
        return perfis.atualizarPerfil(authentication.getName(),request);
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody AlterarSenhaRequest request,Authentication auth) {
        perfis.alterarSenha(auth.getName(),request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> desativar(Authentication auth) {
        perfis.desativarPerfil(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
