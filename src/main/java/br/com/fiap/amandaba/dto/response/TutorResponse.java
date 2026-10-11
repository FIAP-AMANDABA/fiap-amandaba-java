package br.com.fiap.amandaba.dto.response;

import br.com.fiap.amandaba.enums.UserStatus;

import java.util.Date;

public class TutorResponse extends UserResponse{
    Long idTutor;

    public TutorResponse(Long idTutor, Long idUser, String email, String nomeCompleto, String cpf, Date dataCadastro, UserStatus statusUsuario, Date dataNascimento, String telefone) {
        super(idUser, email, nomeCompleto, cpf, dataCadastro, statusUsuario, dataNascimento, telefone);
        this.idTutor = idTutor;
    }
}
