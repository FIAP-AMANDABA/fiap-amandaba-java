package br.com.fiap.amandaba.dto.request;

import java.util.Date;
public class RegisterRequestTutor extends UserRequest {
    public RegisterRequestTutor() { super(); }
    public RegisterRequestTutor(String nomeCompleto, String cpf, String email, String password, String telefone, Date dataNascimento) {
        super(nomeCompleto, cpf, email, password, telefone, dataNascimento);
    }
}
