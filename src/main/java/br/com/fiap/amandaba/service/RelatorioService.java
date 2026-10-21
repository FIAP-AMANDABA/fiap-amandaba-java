package br.com.fiap.amandaba.service;

import br.com.fiap.amandaba.dto.response.RelatorioPetResponse;
import br.com.fiap.amandaba.model.User;
import br.com.fiap.amandaba.repository.TutorRepository;
import br.com.fiap.amandaba.repository.VeterinarioRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class RelatorioService {
    private final EntityManager entityManager;
    private final PerfilService perfilService;
    private final TutorRepository tutores;
    private final VeterinarioRepository veterinarios;

    public RelatorioService(EntityManager entityManager, PerfilService perfilService,
                            TutorRepository tutores, VeterinarioRepository veterinarios) {
        this.entityManager=entityManager;this.perfilService=perfilService;
        this.tutores=tutores;this.veterinarios=veterinarios;
    }

    // Funcao de idade do package Oracle e indicadores de saude; sem dados mockados.
    private static final String SQL_BASE="""
        SELECT P.ID_PET, P.NM_PET, E.NM_ESPECIE,
          CASE WHEN P.DT_NASCIMENTO_PET IS NULL THEN NULL
               ELSE PKG_PET_SAUDE.FN_CALCULAR_IDADE_PET(P.DT_NASCIMENTO_PET) END AS IDADE,
          (SELECT COUNT(*) FROM TAB_PET_DOENCA D WHERE D.ID_PET_DOENCA=P.ID_PET AND D.ST_DOENCA_PET='ATIVA'),
          (SELECT COUNT(*) FROM TAB_PET_MEDICAMENTO M WHERE M.ID_PET_MEDICAMENTO=P.ID_PET AND M.ST_MEDICAMENTO_PET='EM_USO'),
          (SELECT COUNT(*) FROM TAB_PET_VACINA V WHERE V.ID_PET_VACINA=P.ID_PET
                AND V.DT_PROXIMA_DOSE_VACINA < TRUNC(SYSDATE)),
          (SELECT COUNT(*) FROM TAB_CONSULTA C WHERE C.ID_PET_CONSULTA=P.ID_PET)
        FROM TAB_PET P
        INNER JOIN TAB_ESPECIE E ON E.ID_ESPECIE=P.ID_ESPECIE_PET
        """;

    @Transactional(readOnly=true)
    public List<RelatorioPetResponse> relatorioTutor(String email) {
        User usuario=perfilService.usuarioAtivo(email);
        Long tutorId=tutores.findByUserId(usuario.getId())
               .orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN,"Perfil tutor necessario"))
               .getIdTutor();
        String sql=SQL_BASE+" WHERE P.ST_PET='ATIVO' AND P.ID_TUTOR_PET=:perfilId ORDER BY P.NM_PET";
        return consultar(sql,tutorId);
    }

    @Transactional(readOnly=true)
    public List<RelatorioPetResponse> relatorioVeterinario(String email) {
        User usuario=perfilService.usuarioAtivo(email);
        Long vetId=veterinarios.findByUserId(usuario.getId())
               .orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN,"Perfil veterinario necessario"))
               .getId();
        String sql=SQL_BASE+" INNER JOIN TAB_PET_VETERINARIO PV ON PV.ID_PET_COMPARTILHADO=P.ID_PET "
                +" WHERE P.ST_PET='ATIVO' AND PV.ID_VETERINARIO_ACESSO=:perfilId "
                +" AND PV.ST_COMPARTILHAMENTO='ATIVO' ORDER BY P.NM_PET";
        return consultar(sql,vetId);
    }

    @SuppressWarnings("unchecked")
    private List<RelatorioPetResponse> consultar(String sql,Long perfilId) {
        List<Object[]> linhas=entityManager.createNativeQuery(sql).setParameter("perfilId",perfilId).getResultList();
        return linhas.stream().map(l->new RelatorioPetResponse(
            num(l[0]).longValue(),(String)l[1],(String)l[2],
            l[3]==null?null:num(l[3]).intValue(),
            num(l[4]).longValue(),num(l[5]).longValue(),num(l[6]).longValue(),num(l[7]).longValue()
        )).toList();
    }
    private Number num(Object valor) { return (Number)valor; }
}
