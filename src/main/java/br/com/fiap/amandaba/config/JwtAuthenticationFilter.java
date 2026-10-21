package br.com.fiap.amandaba.config;

import br.com.fiap.amandaba.enums.*;
import br.com.fiap.amandaba.model.*;
import br.com.fiap.amandaba.repository.*;
import br.com.fiap.amandaba.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserRepository users;
    private final TutorRepository tutores;
    private final VeterinarioRepository veterinarios;

    public JwtAuthenticationFilter(JwtService jwt, UserRepository users,
                                   TutorRepository tutores, VeterinarioRepository veterinarios) {
        this.jwt=jwt; this.users=users; this.tutores=tutores; this.veterinarios=veterinarios;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
            throws ServletException,IOException {
        String authorization=request.getHeader("Authorization");
        if (authorization!=null && authorization.startsWith("Bearer ") &&
                SecurityContextHolder.getContext().getAuthentication()==null) {
            try {
                Claims claims=jwt.parseAndValidate(authorization.substring(7));
                String email=claims.getSubject();
                String perfil=claims.get("perfil",String.class);
                Number tokenUserId=claims.get("userId", Number.class);
                if (email!=null && tokenUserId!=null && ("TUTOR".equals(perfil) || "VETERINARIO".equals(perfil))) {
                    User user=users.findByEmailIgnoreCase(email).orElse(null);
                    if (user!=null && user.getStatusUsuario()==UserStatus.ATIVO &&
                            user.getId().equals(tokenUserId.longValue()) && perfilAtivo(user.getId(),perfil)) {
                        var authentication=new UsernamePasswordAuthenticationToken(
                                user.getEmail(),null,List.of(new SimpleGrantedAuthority("ROLE_"+perfil)));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request,response);
    }

    private boolean perfilAtivo(Long id,String perfil) {
        if ("TUTOR".equals(perfil)) return tutores.findByUserId(id).isPresent();
        Veterinario vet=veterinarios.findByUserId(id).orElse(null);
        return vet!=null && vet.getVeterinarioStatus()==VeterinarioStatus.ATIVO;
    }
}
