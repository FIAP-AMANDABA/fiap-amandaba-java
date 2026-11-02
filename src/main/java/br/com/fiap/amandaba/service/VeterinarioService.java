package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.response.VeterinarioPublicoResponse;
import br.com.fiap.amandaba.enums.*;
import br.com.fiap.amandaba.model.*;
import br.com.fiap.amandaba.repository.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VeterinarioService {
    private final VeterinarioRepository veterinarios;
    private final UserRepository users;
    public VeterinarioService(VeterinarioRepository veterinarios,UserRepository users) {
        this.veterinarios=veterinarios; this.users=users;
    }

    @Transactional(readOnly=true)
    public Page<VeterinarioPublicoResponse> pesquisar(String nome,String crmv,int pagina,int tamanho) {
        if (pagina<0 || tamanho<1 || tamanho>100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pagina/tamanho invalidos");
        return veterinarios.pesquisar(limpar(nome),limpar(crmv),VeterinarioStatus.ATIVO,UserStatus.ATIVO,
                        PageRequest.of(pagina,tamanho))
                .map(this::publico);
    }

    @Transactional(readOnly=true)
    public VeterinarioPublicoResponse consultar(Long id) {
        Veterinario vet=veterinarios.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Veterinario nao encontrado"));
        User user=users.findById(vet.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario nao encontrado"));
        if (vet.getVeterinarioStatus()!=VeterinarioStatus.ATIVO || user.getStatusUsuario()!=UserStatus.ATIVO)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Veterinario nao encontrado");
        return new VeterinarioPublicoResponse(vet.getId(),user.getNomeCompleto(),vet.getCrmv());
    }

    private VeterinarioPublicoResponse publico(Veterinario vet) {
        User user=users.findById(vet.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario nao encontrado"));
        return new VeterinarioPublicoResponse(vet.getId(),user.getNomeCompleto(),vet.getCrmv());
    }
    private String limpar(String t) { return t==null || t.isBlank() ? null : t.trim(); }
}
