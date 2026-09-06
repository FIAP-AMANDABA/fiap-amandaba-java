package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.LoginRequest;
import br.com.fiap.amandaba.dto.RegisterRequest;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(request, passwordEncoder);
        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {

            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}