package br.com.fiap.amandaba.model;

import br.com.fiap.amandaba.dto.RegisterRequestVeterinario;
import br.com.fiap.amandaba.enums.VeterinarioStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "TAB_VETERINARIO")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VETERINARIO")
    private Long id;

    @Column(nullable = false, name = "ID_USUARIO_VETERINARIO")
    private Long userId;

    @Column(nullable = false, name = "NR_CRMV_VETERINARIO")
    private String crmv;

    @Column(nullable = false, name = "ST_VETERINARIO")
    private VeterinarioStatus veterinarioStatus;

    public Veterinario(RegisterRequestVeterinario registerRequestVeterinario, Long userId) {
        this.userId = userId;
        this.crmv = registerRequestVeterinario.getCrmv_veterinario();
        this.veterinarioStatus = VeterinarioStatus.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public VeterinarioStatus getVeterinarioStatus() {
        return veterinarioStatus;
    }

    public void setVeterinarioStatus(VeterinarioStatus veterinarioStatus) {
        this.veterinarioStatus = veterinarioStatus;
    }


}