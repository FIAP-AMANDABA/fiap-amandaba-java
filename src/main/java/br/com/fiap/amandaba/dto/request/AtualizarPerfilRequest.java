package br.com.fiap.amandaba.dto.request;

import jakarta.validation.constraints.*;
import java.util.Date;

public record AtualizarPerfilRequest(
    @NotBlank @Size(max=150) String nomeCompleto,
    @NotBlank @Email @Size(max=150) String email,
    @Size(max=20) String telefone,
    @Past Date dataNascimento
) {}
