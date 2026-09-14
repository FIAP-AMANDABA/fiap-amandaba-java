package br.com.fiap.amandaba.dto;

import java.util.Date;

public record UserResponse(
        Long id,
        String email,
        Date dataNascimento,
        String nomeCompleto,
        String cpf,
        String telefone,
        Date dataCadastro,
        String statusUsuario
) {
}
