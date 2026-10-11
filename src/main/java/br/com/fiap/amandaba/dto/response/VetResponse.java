package br.com.fiap.amandaba.dto.response;

import br.com.fiap.amandaba.enums.UserStatus;
import br.com.fiap.amandaba.enums.VeterinarioStatus;

import java.util.Date;

public class VetResponse extends UserResponse{
    Long idVeterinario;
    String crmv;
    VeterinarioStatus veterinarioStatus;

    public VetResponse(Long idVeterinario, String crmv, VeterinarioStatus veterinarioStatus,Long idUser, String email, String nomeCompleto, String cpf, Date dataCadastro, UserStatus statusUsuario, Date dataNascimento, String telefone) {
        super(idUser, email, nomeCompleto, cpf, dataCadastro, statusUsuario, dataNascimento, telefone);
        this.idVeterinario = idVeterinario;
        this.crmv = crmv;
        this.veterinarioStatus = veterinarioStatus;
    }
}
