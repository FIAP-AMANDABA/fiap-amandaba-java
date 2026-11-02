package br.com.fiap.amandaba.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TAB_TUTOR")
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TUTOR")
    private Long idTutor;

    @Column(name = "ID_USUARIO_TUTOR", nullable = false, unique = true)
    private Long userId;

    public Tutor() {}
    public Tutor(Long userId) { this.userId = userId; }
    public Long getIdTutor() { return idTutor; }
    public void setIdTutor(Long idTutor) { this.idTutor = idTutor; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
