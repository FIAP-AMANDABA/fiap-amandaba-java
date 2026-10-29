package br.com.fiap.amandaba.dto;

import java.util.Date;

public record UpdateUserRequest(
        String nomeCompleto,
        String telefone,
        Date dataNascimento
) {}