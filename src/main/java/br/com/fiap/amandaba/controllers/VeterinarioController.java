package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.request.*;
import br.com.fiap.amandaba.dto.response.*;
import br.com.fiap.amandaba.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {
    private final PerfilService perfis;
    private final VeterinarioService veterinarios;
    public VeterinarioController(PerfilService perfis, VeterinarioService veterinarios) {
        this.perfis=perfis; this.veterinarios=veterinarios;
    }

    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/me")
    public UserResponse consultarMeuPerfil(Authentication auth) { return perfis.meuPerfil(auth.getName()); }

    @PreAuthorize("hasRole('VETERINARIO')")
    @PutMapping("/me")
    public UserResponse atualizarMeuPerfil(@Valid @RequestBody AtualizarPerfilRequest request,Authentication auth) {
        return perfis.atualizarPerfil(auth.getName(),request);
    }

    @PreAuthorize("hasRole('VETERINARIO')")
    @PutMapping("/me/crmv")
    public VetResponse atualizarCrmv(@Valid @RequestBody AtualizarCrmvRequest request,Authentication auth) {
        return perfis.atualizarCrmv(auth.getName(),request);
    }

    @GetMapping
    public Page<VeterinarioPublicoResponse> pesquisar(@RequestParam(required=false) String nome,
            @RequestParam(required=false) String crmv,@RequestParam(defaultValue="0") int pagina,
            @RequestParam(defaultValue="10") int tamanho) {
        return veterinarios.pesquisar(nome,crmv,pagina,tamanho);
    }

    @GetMapping("/{id}")
    public VeterinarioPublicoResponse consultar(@PathVariable Long id) {
        return veterinarios.consultar(id);
    }
}
