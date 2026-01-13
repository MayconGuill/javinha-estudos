package maycon.guill.userservice.service;

import lombok.RequiredArgsConstructor;
import maycon.guill.userservice.mappers.UserProfileMapper;
import maycon.guill.userservice.repositories.UserProfileRepository;
import maycon.guill.userservice.response.UserProfileGetResponseDTO;
import maycon.guill.userservice.response.UserProfileUserGetResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;

    public List<UserProfileGetResponseDTO> findAll() {
        var userProfiles = repository.findAll();

        return mapper.toUserProfileGetResponseDTO(userProfiles);
    }

    public List<UserProfileUserGetResponseDTO> findByUsersProfileId(Long id) {
        var users = repository.findByUsersProfileId(id);

        return mapper.toUserProfileUserGetResponseDTO(users);
    }
}
