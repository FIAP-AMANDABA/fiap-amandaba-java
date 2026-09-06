package br.com.fiap.amandaba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<br.com.fiap.model.User, Long> {

    Optional<br.com.fiap.model.User> findByEmail(String email);
}