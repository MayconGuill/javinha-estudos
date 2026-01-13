package maycon.guill.userservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maycon.guill.userservice.domain.UserProfile;
import maycon.guill.userservice.response.UserProfileGetResponseDTO;
import maycon.guill.userservice.response.UserProfileUserGetResponseDTO;
import maycon.guill.userservice.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-profile")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {
    private final UserProfileService service;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponseDTO>> findAll() {
        log.debug("Requisição recebida para listar todos os perfis de usuários");

        var userProfileList = service.findAll();

        return ResponseEntity.ok(userProfileList);
    }

    @GetMapping("profile/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponseDTO>> findByUsersProfileId(@PathVariable Long id) {
        log.debug("Requisição recebida para listar todos os perfis de usuários com o mesmo profile.");

        var byProfileId = service.findByUsersProfileId(id);

        return ResponseEntity.ok(byProfileId);
    }
}
