package br.com.fiap.amandaba.dto.request;

import jakarta.validation.constraints.*;
import java.util.Date;

public abstract class UserRequest {
    @NotBlank @Size(max=150)
    private String nomeCompleto;
    @NotBlank @Pattern(regexp="[0-9]{11}", message="CPF deve conter 11 digitos numericos")
    private String cpf;
    @NotBlank @Email @Size(max=150)
    private String email;
    @NotBlank @Size(min=8, max=100)
    private String password;
    @Size(max=20)
    private String telefone;
    @Past
    private Date dataNascimento;

    public UserRequest() {}
    public UserRequest(String nomeCompleto, String cpf, String email, String password, String telefone, Date dataNascimento) {
        this.nomeCompleto=nomeCompleto; this.cpf=cpf; this.email=email;
        this.password=password; this.telefone=telefone; this.dataNascimento=dataNascimento;
    }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto=nomeCompleto; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf=cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password=password; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone=telefone; }
    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento=dataNascimento; }
}
