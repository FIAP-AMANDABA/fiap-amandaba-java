package br.com.fiap.amandaba.dto;

import java.util.Date;

public record UserDetailResponse(
        Long id,
        String email,
        String nomeCompleto,
        String cpf,
        String telefone,
        Date dataNascimento,
        String statusUsuario,
        Long idTutor
) {}