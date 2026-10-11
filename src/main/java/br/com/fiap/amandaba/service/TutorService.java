package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.request.RegisterRequestTutor;
import br.com.fiap.amandaba.model.Tutor;
import br.com.fiap.amandaba.repository.TutorRepository;
import org.hibernate.QueryException;
import org.springframework.stereotype.Service;

@Service
public class TutorService {
    private final TutorRepository tutorRepository;

    public TutorService(
            TutorRepository tutorRepository
    ){
        this.tutorRepository = tutorRepository;
    }

    public Tutor tutorRegister(RegisterRequestTutor registerRequestTutor, Long userId) {
        Tutor tutor = new Tutor(registerRequestTutor, userId);
        return tutor;
    }

    public Tutor getTutor(Long userId){
        Tutor tutor = tutorRepository.findByUserId(userId).orElseThrow(()-> new QueryException("Erro na busca por tutor"));
        return tutor;
    }
}
