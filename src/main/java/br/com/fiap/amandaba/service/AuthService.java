package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.request.LoginRequest;
import br.com.fiap.amandaba.dto.request.RegisterRequestTutor;
import br.com.fiap.amandaba.dto.request.RegisterRequestVeterinario;
import br.com.fiap.amandaba.dto.request.UserRequest;
import br.com.fiap.amandaba.dto.response.TutorResponse;
import br.com.fiap.amandaba.dto.response.UserResponse;
import br.com.fiap.amandaba.dto.response.VetResponse;
import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.model.Veterinario;
import br.com.fiap.amandaba.repository.TutorRepository;
import br.com.fiap.amandaba.repository.UserRepository;
import org.hibernate.QueryException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TutorRepository tutorRepository;
    private final VeterinarioService veterinarioService;
    private final TutorService tutorService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TutorRepository tutorRepository,
            VeterinarioService veterinarioService,
            TutorService tutorService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tutorRepository = tutorRepository;
        this.veterinarioService = veterinarioService;
        this.tutorService = tutorService;
    }

    public User getUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User não foi encontrado"));
        return user;
    }

    public User register(UserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(request, passwordEncoder);
        userRepository.save(user);

        User userRegistered = userRepository.findByEmail(user.getEmail()).orElseThrow(
                ()-> new RuntimeException("User não foi criado com sucesso")
        );

        Tutor tutor = new Tutor(userRegistered.getId());

        tutorRepository.save(tutor);

        return user;
    }

    public UserResponse login(LoginRequest request) {
        User user = getUser(request.email());
        if (user == null){
            return null;
        }
        Tutor tutor = tutorService.getTutor(user.getId());
        Veterinario veterinario = veterinarioService.getVeterinarioByUserId(user.getId());
        if(tutor == null){
            VetResponse vetResponse = new VetResponse(veterinario, user);
            return vetResponse;
        }
        TutorResponse tutorResponse = new TutorResponse(tutor, user);
        return tutorResponse;
    }

    public VetResponse registerVeterinario(RegisterRequestVeterinario request){

        User user = register(request);
        Veterinario veterinarioCriado = veterinarioService.veterinarioRegister(request, user.getId());

        VetResponse userResponse = new VetResponse(
                veterinarioCriado.getId(),
                veterinarioCriado.getCrmv(),
                veterinarioCriado.getVeterinarioStatus(),

                user.getId(),
                user.getEmail(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getDataCadastro(),
                user.getStatusUsuario(),

                user.getDataNascimento(),
                user.getTelefone()
        );
        return userResponse;
    }

    public TutorResponse registerTutor(RegisterRequestTutor request){
        User user = register(request);
        Tutor tutorCriado = tutorService.tutorRegister(request, user.getId());

        TutorResponse userResponse = new TutorResponse(

                tutorCriado.getIdTutor(),

                user.getId(),
                user.getEmail(),
                user.getNomeCompleto(),
                user.getCpf(),
                user.getDataCadastro(),
                user.getStatusUsuario(),

                user.getDataNascimento(),
                user.getTelefone()
        );
        return userResponse;
    }

}