package br.com.fiap.amandaba.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

public class RegisterRequestVeterinario extends UserRequest {
    @NotBlank @Size(max=20)
    @JsonAlias("crmv")
    private String crmv_veterinario;
    public RegisterRequestVeterinario() { super(); }
    public RegisterRequestVeterinario(String nomeCompleto, String cpf, String email, String password, String telefone, Date dataNascimento) {
        super(nomeCompleto, cpf, email, password, telefone, dataNascimento);
    }
    public String getCrmv_veterinario() { return crmv_veterinario; }
    public void setCrmv_veterinario(String crmv) { this.crmv_veterinario = crmv; }
}
