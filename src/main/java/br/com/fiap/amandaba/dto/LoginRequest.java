package br.com.fiap.amandaba.dto;

public record LoginRequest(
        String email,
        String password
) {
}
