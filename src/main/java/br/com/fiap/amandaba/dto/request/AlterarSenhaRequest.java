package br.com.fiap.amandaba.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record AlterarSenhaRequest(@NotBlank String senhaAtual, @NotBlank @Size(min=8,max=100) String novaSenha) {}
