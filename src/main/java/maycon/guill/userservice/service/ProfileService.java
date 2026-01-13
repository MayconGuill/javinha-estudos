package maycon.guill.userservice.service;

import lombok.RequiredArgsConstructor;
import maycon.guill.userservice.mappers.ProfileMapper;
import maycon.guill.userservice.repositories.ProfileRepository;
import maycon.guill.userservice.request.ProfilePostRequestDTO;
import maycon.guill.userservice.response.ProfileGetResponseDTO;
import maycon.guill.userservice.response.ProfilePostResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;
    private final ProfileMapper mapper;

    public List<ProfileGetResponseDTO> findAll() {
        var profiles = repository.findAll();

        return mapper.toGetResponse(profiles);
    }

    public ProfilePostResponseDTO save(ProfilePostRequestDTO request) {
        var profileToSave = mapper.toProfile(request);

        var profileSaved = repository.save(profileToSave);

        return mapper.toPostResponse(profileSaved);
    }
}
