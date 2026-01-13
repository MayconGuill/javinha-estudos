package maycon.guill.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maycon.guill.userservice.request.UserPostRequestDTO;
import maycon.guill.userservice.request.UserPutRequestDTO;
import maycon.guill.userservice.response.UserGetResponseDTO;
import maycon.guill.userservice.response.UserPostResponseDTO;
import maycon.guill.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserGetResponseDTO>> findAll(@RequestParam(required = false) String firstName) {
        log.debug("Requisição recebida para listar todos os usuários, parâmetrizado por {}", firstName);

        var userList = service.findAll(firstName);

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<UserGetResponseDTO>> findAll(Pageable pageable) {
        log.debug("Requisição recebida para listar os usuários, paginados por {}", pageable);

        var allPaginated = service.findAllPaginated(pageable);

        return ResponseEntity.ok(allPaginated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponseDTO> findById(@PathVariable Long id) {
        log.debug("Requisição recebida para listar um usuário pelo id {}", id);

        var user = service.findById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserPostResponseDTO> save(@RequestBody @Valid UserPostRequestDTO request) {
        log.debug("Requisição recebida para cadastrar um usuário");

        var userSaved = service.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Requisição recebida para deletar um usuário");

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequestDTO request) {
        log.debug("Requisição recebida para atualizar um usuário");

        service.update(request);

        return ResponseEntity.noContent().build();
    }
}
