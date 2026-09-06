package br.com.fiap.amandaba.model;

import br.com.fiap.amandaba.dto.RegisterRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Entity
@Table(name = "TAB_USUARIO")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(nullable = false, unique = true, name = "DS_EMAIL_USUARIO")
    private String email;

    @Column(nullable = false, name = "DS_SENHA_HASH_USUARIO")
    private String password;

    @Column(name = "DT_NASCIMENTO_USUARIO", nullable = false)
    private Date dataNascimento;

    @Column(name = "NM_USUARIO", nullable = false)
    private String nomeCompleto;

    @Column(name = "NR_CPF_USUARIO", nullable = false)
    private String cpf;

    @Column(name = "NR_TELEFONE_USUARIO", nullable = true)
    private String telefone;

    @Column(name = "DT_CADASTRO_USUARIO", nullable = false)
    private Date dataCadastro;

    @Column(name = "ST_USUARIO", nullable = false)
    private String statusUsuario;

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
        this.dataCadastro = new Date();
        this.statusUsuario = "ATIVO";
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
