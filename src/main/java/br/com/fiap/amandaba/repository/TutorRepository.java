package br.com.fiap.amandaba.repository;

import br.com.fiap.amandaba.model.Tutor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor,Long> {
    Optional<Tutor> findByUserId(Long userId);
}
