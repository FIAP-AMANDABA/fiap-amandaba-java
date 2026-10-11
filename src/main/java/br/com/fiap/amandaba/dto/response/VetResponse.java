package br.com.fiap.amandaba.dto.response;

import br.com.fiap.amandaba.enums.UserStatus;
import br.com.fiap.amandaba.enums.VeterinarioStatus;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.model.Veterinario;

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

    public VetResponse(Veterinario veterinario, User user){
        super(user.getId(), user.getEmail(), user.getNomeCompleto(), user.getCpf(), user.getDataCadastro(), user.getStatusUsuario(), user.getDataNascimento(), user.getTelefone());
        this.idVeterinario = veterinario.getId();
        this.crmv = veterinario.getCrmv();
        this.veterinarioStatus = veterinario.getVeterinarioStatus();
    }
}
