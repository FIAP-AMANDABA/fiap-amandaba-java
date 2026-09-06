package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.LoginRequest;
import br.com.fiap.amandaba.dto.RegisterRequest;
import br.com.fiap.amandaba.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        br.com.fiap.model.User user = new br.com.fiap.model.User();

        user.setEmail(request.email());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);
    }

    public boolean login(LoginRequest request) {

        br.com.fiap.model.User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        return passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );
    }
}