package br.com.fiap.amandaba.dto;

import java.util.Date;

public record TutorResponse(
        Long tutorId,
        Long userId,
        String email,
        String password,
        Date dataNascimento,
        String nomeCompleto,
        String cpf,
        String telefone,
        Date dataCadastro,
        String statusUsuario
) { }
