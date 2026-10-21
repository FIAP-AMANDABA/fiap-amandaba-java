package br.com.fiap.amandaba.repository;

import br.com.fiap.amandaba.enums.UserStatus;
import br.com.fiap.amandaba.enums.VeterinarioStatus;
import br.com.fiap.amandaba.model.Veterinario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VeterinarioRepository extends JpaRepository<Veterinario,Long> {
    Optional<Veterinario> findByCrmvIgnoreCase(String crmv);
    boolean existsByCrmvIgnoreCase(String crmv);
    Optional<Veterinario> findByUserId(Long userId);

    @Query("select v from Veterinario v, User u where v.userId=u.id " +
           "and v.veterinarioStatus=:statusVet and u.statusUsuario=:statusUser " +
           "and (:nome is null or lower(u.nomeCompleto) like lower(concat('%', :nome, '%'))) " +
           "and (:crmv is null or lower(v.crmv) like lower(concat('%', :crmv, '%')))")
    Page<Veterinario> pesquisar(@Param("nome") String nome,
                                @Param("crmv") String crmv,
                                @Param("statusVet") VeterinarioStatus statusVet,
                                @Param("statusUser") UserStatus statusUser,
                                Pageable pageable);
}
