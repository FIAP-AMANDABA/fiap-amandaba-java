package br.com.fiap.amandaba.dto;

import java.util.Date;

public class RegisterRequestVeterinario extends UserRequest{
    String crmv_veterinario;

    public RegisterRequestVeterinario(String nomeCompleto, String cpf, String email, String password, String telefone, Date dataNascimento) {
        super(nomeCompleto, cpf, email, password, telefone, dataNascimento);
    }

    public String getCrmv_veterinario() {
        return crmv_veterinario;
    }

    public void setCrmv_veterinario(String crmv_veterinario) {
        this.crmv_veterinario = crmv_veterinario;
    }
}
