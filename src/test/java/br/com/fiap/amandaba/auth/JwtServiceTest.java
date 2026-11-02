package br.com.fiap.amandaba.auth;

import br.com.fiap.amandaba.service.JwtService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    @Test void tokenGerado_ContemIdentidadePerfilEEmissor() {
        JwtService service=new JwtService("teste-de-jwt-com-segredo-de-mais-de-trinta-e-dois-bytes", "amandaba-java", "amandaba-apis",3600000);
        String token=service.generateToken("tutor@exemplo.com",7L,"TUTOR");
        var claims=service.parseAndValidate(token);
        assertEquals("tutor@exemplo.com",claims.getSubject());
        assertEquals("TUTOR",claims.get("perfil",String.class));
        assertEquals(7L,claims.get("userId",Number.class).longValue());
        assertTrue(claims.getAudience().contains("amandaba-apis"));
    }

    @Test void tokenComAssinaturaDeOutraChave_ERejeitado() {
        JwtService a=new JwtService("primeira-chave-forte-de-pelo-menos-32-caracteres-123", "amandaba-java","amandaba-apis",3600000);
        JwtService b=new JwtService("segunda-chave-forte-de-pelo-menos-32-caracteres-123","amandaba-java","amandaba-apis",3600000);
        assertThrows(JwtException.class,()->b.parseAndValidate(a.generateToken("a@exemplo.com",1L,"TUTOR")));
    }

    @Test void chaveMuitoCurta_FalhaNaInicializacao() {
        assertThrows(IllegalArgumentException.class,()->new JwtService("curta","amandaba-java","amandaba-apis",3600000));
    }
}
