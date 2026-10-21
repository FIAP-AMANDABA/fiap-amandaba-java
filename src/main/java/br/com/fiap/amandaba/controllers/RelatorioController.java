package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.response.RelatorioPetResponse;
import br.com.fiap.amandaba.service.RelatorioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    private final RelatorioService relatorios;
    public RelatorioController(RelatorioService relatorios) { this.relatorios=relatorios; }

    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/tutor/me")
    public List<RelatorioPetResponse> tutor(Authentication auth) {
        return relatorios.relatorioTutor(auth.getName());
    }

    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/veterinario/me")
    public List<RelatorioPetResponse> veterinario(Authentication auth) {
        return relatorios.relatorioVeterinario(auth.getName());
    }
}
