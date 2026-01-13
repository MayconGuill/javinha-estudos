package maycon.guill.userservice.service;

import lombok.RequiredArgsConstructor;
import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.exceptions.EmailBadRequestException;
import maycon.guill.userservice.exceptions.UserNotFoundException;
import maycon.guill.userservice.mappers.UserMapper;
import maycon.guill.userservice.repositories.UserRepository;
import maycon.guill.userservice.request.UserPostRequestDTO;
import maycon.guill.userservice.request.UserPutRequestDTO;
import maycon.guill.userservice.response.UserGetResponseDTO;
import maycon.guill.userservice.response.UserPostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserGetResponseDTO> findAll(String firstName) {
        var users = (firstName == null || firstName.isBlank()) ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);

        return mapper.toGetResponse(users);
    }

    public Page<UserGetResponseDTO> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toGetResponse);
    }

    public UserGetResponseDTO findById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return mapper.toGetResponse(user);
    }

    public UserPostResponseDTO save(UserPostRequestDTO request) {
        assertEmailDoesNotExists(request.email());

        var userToSave = mapper.toUser(request);

        var userToSaved = repository.save(userToSave);

        return mapper.toPostResponse(userToSaved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException();
        }

        repository.deleteById(id);
    }

    public void update(UserPutRequestDTO request) {
        var userToUpdate = repository.findById(request.id())
                .orElseThrow(UserNotFoundException::new);

        assertEmailDoesNotExists(request.email(), request.id());

        mapper.updateUser(request, userToUpdate);

        repository.save(userToUpdate);
    }

    private void assertEmailDoesNotExists(String email) {
        repository.findByEmail(email).ifPresent(this::throwNewEmailBadRequest);
    }

    private void assertEmailDoesNotExists(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwNewEmailBadRequest);
    }

    private void throwNewEmailBadRequest(User user) {
        throw new EmailBadRequestException("Email %s já existente. ".formatted(user.getEmail()));
    }
}
