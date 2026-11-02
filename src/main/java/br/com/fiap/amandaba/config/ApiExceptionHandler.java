package br.com.fiap.amandaba.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> validar(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro","Dados invalidos",
            "detalhe",ex.getBindingResult().getFieldErrors().stream().findFirst()
                       .map(e->e.getField()+": "+e.getDefaultMessage()).orElse("Requisicao invalida")));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> conflito(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erro","Registro duplicado ou relacionamento invalido"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,String>> status(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("erro",
                ex.getReason()==null ? "Operacao nao permitida" : ex.getReason()));
    }
}
