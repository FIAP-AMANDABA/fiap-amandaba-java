package br.com.fiap.amandaba.dto.response;

import br.com.fiap.amandaba.enums.UserStatus;
import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.model.User;

import java.util.Date;

public class TutorResponse extends UserResponse{
    Long idTutor;

    public TutorResponse(Long idTutor, Long idUser, String email, String nomeCompleto, String cpf, Date dataCadastro, UserStatus statusUsuario, Date dataNascimento, String telefone) {
        super(idUser, email, nomeCompleto, cpf, dataCadastro, statusUsuario, dataNascimento, telefone);
        this.idTutor = idTutor;
    }

    public TutorResponse (Tutor tutor, User user){
        super(user.getId(), user.getEmail(), user.getNomeCompleto(), user.getCpf(), user.getDataCadastro(), user.getStatusUsuario(), user.getDataNascimento(), user.getTelefone());
        this.idTutor = tutor.getIdTutor();
    }
}
