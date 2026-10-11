package br.com.fiap.amandaba.model;

import br.com.fiap.amandaba.dto.request.RegisterRequestTutor;
import jakarta.persistence.*;

@Entity
@Table(name = "TAB_TUTOR")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TUTOR")
    private Long idTutor;

    @Column(nullable = false, name = "ID_USUARIO_TUTOR")
    private Long userId;

    public Tutor(RegisterRequestTutor registerRequestTutor, Long user){
        this.userId = user;
    }

    public Tutor(Long idTutor, Long userId) {
        this.idTutor = idTutor;
        this.userId = userId;
    }

    public Tutor() {
    }

    public Tutor(Long user) {
        this.userId = user;
    }

    public Long getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(Long idTutor) {
        this.idTutor = idTutor;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
