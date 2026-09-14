package br.com.fiap.amandaba.model;

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

    public Tutor(Long user) {
        this.userId = user;
    }
}
