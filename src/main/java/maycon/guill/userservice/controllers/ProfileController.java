package maycon.guill.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maycon.guill.userservice.request.ProfilePostRequestDTO;
import maycon.guill.userservice.response.ProfileGetResponseDTO;
import maycon.guill.userservice.response.ProfilePostResponseDTO;
import maycon.guill.userservice.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService service;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponseDTO>> findAll() {
        log.debug("Requisição recebida para listar todos os profiles");

        var profileList = service.findAll();

        return ResponseEntity.ok(profileList);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponseDTO> save(@RequestBody @Valid ProfilePostRequestDTO request) {
        log.debug("Requisição recebida para cadastrar um profile");

        var profileSaved = service.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(profileSaved);
    }
}
