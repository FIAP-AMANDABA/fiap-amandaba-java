package br.com.fiap.amandaba.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
