package br.com.fiap.amandaba.controllers;

import br.com.fiap.amandaba.dto.UpdateUserRequest;
import br.com.fiap.amandaba.dto.UserDetailResponse;
import br.com.fiap.amandaba.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(userService.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailResponse> atualizar(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        userService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}