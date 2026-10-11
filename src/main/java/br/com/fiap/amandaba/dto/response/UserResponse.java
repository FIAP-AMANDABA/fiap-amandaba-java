package br.com.fiap.amandaba.dto.response;

import br.com.fiap.amandaba.enums.UserStatus;

import java.util.Date;

public abstract class UserResponse {
    Long idUser;
    String email;
    String nomeCompleto;
    String cpf;
    Date dataCadastro;
    UserStatus statusUsuario;

    Date dataNascimento;
    String telefone;

    public UserResponse(Long idUser, String email, String nomeCompleto, String cpf, Date dataCadastro, UserStatus statusUsuario, Date dataNascimento, String telefone) {
        this.idUser = idUser;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataCadastro = dataCadastro;
        this.statusUsuario = statusUsuario;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public UserStatus getStatusUsuario() {
        return statusUsuario;
    }

    public void setStatusUsuario(UserStatus statusUsuario) {
        this.statusUsuario = statusUsuario;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
