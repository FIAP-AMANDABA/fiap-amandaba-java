package br.com.fiap.amandaba.auth;

import br.com.fiap.amandaba.dto.request.*;
import br.com.fiap.amandaba.dto.response.*;
import br.com.fiap.amandaba.enums.*;
import br.com.fiap.amandaba.model.*;
import br.com.fiap.amandaba.repository.*;
import br.com.fiap.amandaba.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository users;
    @Mock TutorRepository tutores;
    @Mock VeterinarioRepository vets;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwt;
    AuthService service;

    @BeforeEach void configurar() { service=new AuthService(users,tutores,vets,encoder,jwt); }

    private User ativo() {
        User u=new User();u.setId(5L);u.setEmail("tutor@exemplo.com");u.setPassword("hash");
        u.setStatusUsuario(UserStatus.ATIVO);return u;
    }

    @Test void login_TutorValido_RetornaTokenEIdentificadores() {
        // Arrange
        User u=ativo();Tutor t=new Tutor(5L);t.setIdTutor(11L);
        when(users.findByEmailIgnoreCase("tutor@exemplo.com")).thenReturn(Optional.of(u));
        when(encoder.matches("senha-correta", "hash")).thenReturn(true);
        when(tutores.findByUserId(5L)).thenReturn(Optional.of(t));
        when(vets.findByUserId(5L)).thenReturn(Optional.empty());
        when(jwt.generateToken("tutor@exemplo.com",5L,"TUTOR")).thenReturn("jwt-valido");
        // Act
        AuthResponse response=service.login(new LoginRequest("tutor@exemplo.com","senha-correta"));
        // Assert
        assertEquals("jwt-valido",response.token());
        assertEquals("TUTOR",response.perfil());
        assertEquals(5L,response.idUsuario());
        assertEquals(11L,response.idPerfil());
    }

    @Test void login_SenhaIncorreta_NegaAcesso() {
        // Arrange
        when(users.findByEmailIgnoreCase("tutor@exemplo.com")).thenReturn(Optional.of(ativo()));
        when(encoder.matches("senha-errada","hash")).thenReturn(false);
        // Act + Assert
        assertThrows(ResponseStatusException.class,()->service.login(new LoginRequest("tutor@exemplo.com","senha-errada")));
        verifyNoInteractions(jwt);
    }

    @Test void login_UsuarioInativo_NegaAcesso() {
        User u=ativo();u.setStatusUsuario(UserStatus.INATIVO);
        when(users.findByEmailIgnoreCase("tutor@exemplo.com")).thenReturn(Optional.of(u));
        assertThrows(ResponseStatusException.class,()->service.login(new LoginRequest("tutor@exemplo.com","senha")));
        verifyNoInteractions(jwt);
    }

    @Test void cadastrarTutor_CriaApenasTutor() {
        RegisterRequestTutor req=new RegisterRequestTutor("Ana","12345678901","ANA@EXEMPLO.COM","senha-segura","11999990000",null);
        when(encoder.encode("senha-segura")).thenReturn("hash-bcrypt");
        when(users.save(any(User.class))).thenAnswer(c->{ User u=c.getArgument(0);u.setId(13L);return u; });
        when(tutores.save(any(Tutor.class))).thenAnswer(c->{ Tutor t=c.getArgument(0);t.setIdTutor(21L);return t; });
        TutorResponse retorno=service.registerTutor(req);
        assertEquals(21L,retorno.getIdTutor());
        assertEquals("ana@exemplo.com",retorno.getEmail());
        verifyNoInteractions(vets);
    }

    @Test void cadastrarVet_CriaApenasVeterinario() {
        RegisterRequestVeterinario req=new RegisterRequestVeterinario("Bia","12345678901","bia@exemplo.com","senha-segura",null,null);
        req.setCrmv_veterinario("CRMV-SP-10");
        when(encoder.encode("senha-segura")).thenReturn("hash-bcrypt");
        when(users.save(any(User.class))).thenAnswer(c->{ User u=c.getArgument(0);u.setId(31L);return u; });
        when(vets.save(any(Veterinario.class))).thenAnswer(c->{ Veterinario v=c.getArgument(0);v.setId(41L);return v; });
        VetResponse retorno=service.registerVeterinario(req);
        assertEquals(41L,retorno.getIdVeterinario());
        assertEquals("CRMV-SP-10",retorno.getCrmv());
        verifyNoInteractions(tutores);
    }

    @Test void cadastrarTutor_EmailExistente_RetornaConflito() {
        RegisterRequestTutor req=new RegisterRequestTutor("Ana","12345678901","ana@exemplo.com","senha-segura",null,null);
        when(users.existsByEmailIgnoreCase("ana@exemplo.com")).thenReturn(true);
        assertThrows(ResponseStatusException.class,()->service.registerTutor(req));
        verify(users,never()).save(any(User.class));
    }
}
