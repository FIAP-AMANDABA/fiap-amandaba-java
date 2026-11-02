package br.com.fiap.amandaba.model;

import br.com.fiap.amandaba.enums.VeterinarioStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "TAB_VETERINARIO")
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VETERINARIO")
    private Long id;

    @Column(name = "ID_USUARIO_VETERINARIO", nullable = false, unique = true)
    private Long userId;

    @Column(name = "NR_CRMV_VETERINARIO", nullable = false, unique = true, length = 20)
    private String crmv;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_VETERINARIO", nullable = false, length = 20)
    private VeterinarioStatus veterinarioStatus;

    public Veterinario() {}
    public Veterinario(Long userId, String crmv) {
        this.userId = userId;
        this.crmv = crmv;
        this.veterinarioStatus = VeterinarioStatus.ATIVO;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCrmv() { return crmv; }
    public void setCrmv(String crmv) { this.crmv = crmv; }
    public VeterinarioStatus getVeterinarioStatus() { return veterinarioStatus; }
    public void setVeterinarioStatus(VeterinarioStatus veterinarioStatus) { this.veterinarioStatus = veterinarioStatus; }
}
