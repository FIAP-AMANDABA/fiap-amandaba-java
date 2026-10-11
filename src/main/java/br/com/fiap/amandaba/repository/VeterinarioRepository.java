package br.com.fiap.amandaba.repository;

import br.com.fiap.amandaba.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByCrmv(String crmv);

    Optional<Veterinario> findByUserId(Long userId);
}