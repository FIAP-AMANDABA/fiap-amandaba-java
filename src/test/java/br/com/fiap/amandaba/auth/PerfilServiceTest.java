package br.com.fiap.amandaba.auth;

import br.com.fiap.amandaba.dto.request.*;
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
class PerfilServiceTest {
    @Mock UserRepository users;
    @Mock TutorRepository tutores;
    @Mock VeterinarioRepository vets;
    @Mock PasswordEncoder encoder;
    PerfilService service;
    @BeforeEach void configurar() { service=new PerfilService(users,tutores,vets,encoder); }

    private User user() {
        User u=new User();u.setId(10L);u.setEmail("a@exemplo.com");
        u.setPassword("hash");u.setStatusUsuario(UserStatus.ATIVO);return u;
    }

    @Test void alterarSenha_SenhaAtualIncorreta_Rejeita() {
        when(users.findByEmailIgnoreCase("a@exemplo.com")).thenReturn(Optional.of(user()));
        when(encoder.matches("incorreta","hash")).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->service.alterarSenha("a@exemplo.com",new AlterarSenhaRequest("incorreta","nova-senha123")));
        verify(users,never()).save(any());
    }

    @Test void desativarPerfil_MarcaUsuarioInativo() {
        User u=user();
        when(users.findByEmailIgnoreCase("a@exemplo.com")).thenReturn(Optional.of(u));
        service.desativarPerfil("a@exemplo.com");
        assertEquals(UserStatus.INATIVO,u.getStatusUsuario());
        verify(users).save(u);
    }

    @Test void editarEmail_QuandoDuplicado_RetornaConflito() {
        when(users.findByEmailIgnoreCase("a@exemplo.com")).thenReturn(Optional.of(user()));
        when(users.existsByEmailIgnoreCase("existente@exemplo.com")).thenReturn(true);
        assertThrows(ResponseStatusException.class,()->service.atualizarPerfil("a@exemplo.com",
            new AtualizarPerfilRequest("Ana","existente@exemplo.com",null,null)));
        verify(users,never()).save(any());
    }
}
