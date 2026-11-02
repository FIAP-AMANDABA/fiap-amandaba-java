package br.com.fiap.amandaba.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record AtualizarCrmvRequest(@NotBlank @Size(max=20) String crmv) {}
