package br.com.fiap.amandaba.dto.response;

public record AuthResponse(String token, String tipo, String perfil, Long idUsuario, Long idPerfil) {}
