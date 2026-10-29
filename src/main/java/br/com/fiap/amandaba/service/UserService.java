package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.UpdateUserRequest;
import br.com.fiap.amandaba.dto.UserDetailResponse;
import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.repository.TutorRepository;
import br.com.fiap.amandaba.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;

    public UserService(UserRepository userRepository, TutorRepository tutorRepository) {
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
    }

    public UserDetailResponse buscar(Long id) {
        return toResponse(buscarUsuario(id));
    }

    @Transactional
    public UserDetailResponse atualizar(Long id, UpdateUserRequest request) {
        User user = buscarUsuario(id);

        if (request.nomeCompleto() != null) user.setNomeCompleto(request.nomeCompleto());
        if (request.telefone() != null) user.setTelefone(request.telefone());
        if (request.dataNascimento() != null) user.setDataNascimento(request.dataNascimento());

        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void excluir(Long id) {
        User user = buscarUsuario(id);
        tutorRepository.findByUserId(id).ifPresent(tutorRepository::delete);
        tutorRepository.flush();

        userRepository.delete(user);
    }

    private User buscarUsuario(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private UserDetailResponse toResponse(User user) {
        Long idTutor = tutorRepository.findByUserId(user.getId())
                .map(Tutor::getIdTutor)
                .orElse(null);

        return new UserDetailResponse(
                user.getId(),
                user.getEmail(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getTelefone(),
                user.getDataNascimento(),
                user.getStatusUsuario(),
                idTutor
        );
    }
}