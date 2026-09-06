package br.com.fiap.amandaba.model;

import br.com.fiap.amandaba.dto.RegisterRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Entity
@Table(name = "aman_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    private Date dataNascimento;

    @Column(name = "NOME_COMPLETO", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = true)
    private String telefone;

    public User(Long id, String email, String password, Date dataNascimento, String nomeCompleto, String cpf, String telefone) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dataNascimento = dataNascimento;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public User(RegisterRequest request, PasswordEncoder passwordEncoder) {
        this.email = request.email();
        this.password = passwordEncoder.encode(request.password());
        this.dataNascimento = request.dataNascimento();
        this.nomeCompleto = request.nomeCompleto();
        this.cpf = request.cpf();
        this.telefone = request.telefone();
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
