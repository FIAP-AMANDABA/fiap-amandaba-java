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
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository users;
    private final TutorRepository tutores;
    private final VeterinarioRepository veterinarios;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository users, TutorRepository tutores, VeterinarioRepository veterinarios,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users=users; this.tutores=tutores; this.veterinarios=veterinarios;
        this.passwordEncoder=passwordEncoder; this.jwtService=jwtService;
    }

    @Transactional
    public TutorResponse registerTutor(RegisterRequestTutor request) {
        User user = criarUsuario(request);
        Tutor tutor = tutores.save(new Tutor(user.getId()));
        return new TutorResponse(tutor,user);
    }

    @Transactional
    public VetResponse registerVeterinario(RegisterRequestVeterinario request) {
        if (veterinarios.existsByCrmvIgnoreCase(request.getCrmv_veterinario().trim()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"CRMV ja cadastrado");
        User user = criarUsuario(request);
        Veterinario vet = veterinarios.save(new Veterinario(user.getId(), request.getCrmv_veterinario().trim()));
        return new VetResponse(vet,user);
    }

    private User criarUsuario(UserRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"E-mail ja cadastrado");
        if (users.existsByCpf(request.getCpf()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"CPF ja cadastrado");
        User user = new User();
        user.setNomeCompleto(request.getNomeCompleto().trim());
        user.setCpf(request.getCpf());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTelefone(request.getTelefone());
        user.setDataNascimento(request.getDataNascimento());
        user.setDataCadastro(new Date());
        user.setStatusUsuario(UserStatus.ATIVO);
        return users.save(user);
    }

    @Transactional(readOnly=true)
    public AuthResponse login(LoginRequest request) {
        User user = users.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais invalidas"));
        if (user.getStatusUsuario()!=UserStatus.ATIVO ||
            !passwordEncoder.matches(request.password(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais invalidas");
        Tutor tutor = tutores.findByUserId(user.getId()).orElse(null);
        Veterinario vet = veterinarios.findByUserId(user.getId()).orElse(null);
        if ((tutor==null && vet==null) || (tutor!=null && vet!=null))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Perfil de usuario invalido ou ambiguo");
        if (vet!=null && vet.getVeterinarioStatus()!=VeterinarioStatus.ATIVO)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Veterinario inativo");
        String perfil = tutor!=null ? "TUTOR" : "VETERINARIO";
        Long idPerfil = tutor!=null ? tutor.getIdTutor() : vet.getId();
        return new AuthResponse(jwtService.generateToken(user.getEmail(),user.getId(),perfil),
                "Bearer",perfil,user.getId(),idPerfil);
    }
}
