package br.com.fiap.amandaba.dto.response;

public record RelatorioPetResponse(
        Long idPet, String nomePet, String especie, Integer idadeAnos,
        Long doencasAtivas, Long medicamentosEmUso, Long vacinasAtrasadas, Long consultasRegistradas
) {}
