package br.com.fiap.amandaba.model;

import br.com.fiap.amandaba.enums.UserStatus;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TAB_USUARIO")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NM_USUARIO", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(name = "NR_CPF_USUARIO", nullable = false, unique = true, length = 11)
    private String cpf;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_NASCIMENTO_USUARIO")
    private Date dataNascimento;

    @Column(name = "DS_EMAIL_USUARIO", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "NR_TELEFONE_USUARIO", length = 20)
    private String telefone;

    @Column(name = "DS_SENHA_HASH_USUARIO", nullable = false, length = 255)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CADASTRO_USUARIO", nullable = false)
    private Date dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_USUARIO", nullable = false, length = 20)
    private UserStatus statusUsuario;

    public User() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Date getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }
    public UserStatus getStatusUsuario() { return statusUsuario; }
    public void setStatusUsuario(UserStatus statusUsuario) { this.statusUsuario = statusUsuario; }
}
