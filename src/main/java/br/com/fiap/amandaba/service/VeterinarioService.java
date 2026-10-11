package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.request.RegisterRequestVeterinario;
import br.com.fiap.amandaba.model.Veterinario;
import br.com.fiap.amandaba.repository.VeterinarioRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class VeterinarioService {
    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioService(
            VeterinarioRepository veterinarioRepository
    ){
        this.veterinarioRepository = veterinarioRepository;
    }

    public Veterinario veterinarioRegister(RegisterRequestVeterinario registerRequestVeterinario, Long userId){
        if (veterinarioRepository.findByCrmv(registerRequestVeterinario.getCrmv_veterinario()).isPresent()){
            throw new DuplicateKeyException("CRMV já cadastrado no sistema");
        }
        if (veterinarioRepository.findByUserId(userId.longValue()).isPresent()){
            throw new DuplicateKeyException("Id de usuário já vinculado a um cadastro de veterinário");
        }

        Veterinario veterinario = new Veterinario(registerRequestVeterinario, userId);
        veterinarioRepository.save(veterinario);

        return veterinario;
    }

}
