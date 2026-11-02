package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.request.*;
import br.com.fiap.amandaba.dto.response.*;
import br.com.fiap.amandaba.enums.*;
import br.com.fiap.amandaba.model.*;
import br.com.fiap.amandaba.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PerfilService {
    private final UserRepository users;
    private final TutorRepository tutores;
    private final VeterinarioRepository veterinarios;
    private final PasswordEncoder encoder;

    public PerfilService(UserRepository users, TutorRepository tutores,
                         VeterinarioRepository veterinarios, PasswordEncoder encoder) {
        this.users=users; this.tutores=tutores; this.veterinarios=veterinarios; this.encoder=encoder;
    }

    @Transactional(readOnly=true)
    public UserResponse meuPerfil(String email) {
        User user = usuarioAtivo(email);
        Tutor tutor = tutores.findByUserId(user.getId()).orElse(null);
        if (tutor!=null) return new TutorResponse(tutor,user);
        Veterinario vet = veterinarios.findByUserId(user.getId()).orElse(null);
        if (vet!=null) return new VetResponse(vet,user);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Usuario sem perfil");
    }

    @Transactional
    public UserResponse atualizarPerfil(String email, AtualizarPerfilRequest request) {
        User user=usuarioAtivo(email);
        String emailNovo=request.email().trim().toLowerCase();
        if (!emailNovo.equalsIgnoreCase(user.getEmail()) && users.existsByEmailIgnoreCase(emailNovo))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"E-mail ja cadastrado");
        user.setNomeCompleto(request.nomeCompleto().trim());
        user.setEmail(emailNovo);
        user.setTelefone(request.telefone());
        user.setDataNascimento(request.dataNascimento());
        users.save(user);
        return meuPerfil(user.getEmail());
    }

    @Transactional
    public void alterarSenha(String email, AlterarSenhaRequest request) {
        User user=usuarioAtivo(email);
        if (!encoder.matches(request.senhaAtual(),user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Senha atual incorreta");
        user.setPassword(encoder.encode(request.novaSenha()));
        users.save(user);
    }

    @Transactional
    public void desativarPerfil(String email) {
        User user=usuarioAtivo(email);
        user.setStatusUsuario(UserStatus.INATIVO);
        users.save(user);
    }

    @Transactional
    public VetResponse atualizarCrmv(String email, AtualizarCrmvRequest request) {
        User user=usuarioAtivo(email);
        Veterinario vet=veterinarios.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,"Nao e veterinario"));
        String novoCrmv=request.crmv().trim();
        if (!novoCrmv.equalsIgnoreCase(vet.getCrmv()) && veterinarios.existsByCrmvIgnoreCase(novoCrmv))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"CRMV ja cadastrado");
        vet.setCrmv(novoCrmv);
        veterinarios.save(vet);
        return new VetResponse(vet,user);
    }

    public User usuarioAtivo(String email) {
        User user=users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario nao encontrado"));
        if (user.getStatusUsuario()!=UserStatus.ATIVO)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Usuario inativo");
        return user;
    }
}
