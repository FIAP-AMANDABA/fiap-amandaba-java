package br.com.fiap.amandaba.dto;

import java.util.Date;

public record RegisterRequest(
        String nomeCompleto,
        String cpf,
        Date dataNascimento,
        String email,
        String telefone,
        String password
) {}