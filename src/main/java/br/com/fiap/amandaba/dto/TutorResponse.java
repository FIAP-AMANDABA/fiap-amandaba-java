package br.com.fiap.amandaba.dto;

import br.com.fiap.amandaba.enums.UserStatus;
import br.com.fiap.amandaba.enums.VeterinarioStatus;

import java.util.Date;

public record TutorResponse(

        Long idTutor,

        Long idUser,
        String email,
        String nomeCompleto,
        String cpf,
        Date dataCadastro,
        UserStatus statusUsuario,

        Date dataNascimento,
        String telefone
) {
}
