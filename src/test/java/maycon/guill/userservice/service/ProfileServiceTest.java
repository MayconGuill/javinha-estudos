package maycon.guill.userservice.service;

import maycon.guill.userservice.commons.ProfileUtils;
import maycon.guill.userservice.domain.Profile;
import maycon.guill.userservice.mappers.ProfileMapper;
import maycon.guill.userservice.repositories.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;

    @Mock
    private ProfileRepository repository;

    @Mock
    private ProfileMapper mapper;

    private ProfileUtils profileUtils;

    private List<Profile> profileList;

    @BeforeEach
    void init() {
        profileUtils = new ProfileUtils();
        profileList = profileUtils.profileList();
    }

    @Test
    @DisplayName("findAll DEVE retornar todos os profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        var profileListResponseDTO = profileUtils.profileListResponseDTO();

        BDDMockito.when(repository.findAll()).thenReturn(profileList);
        BDDMockito.when(mapper.toGetResponse(profileList)).thenReturn(profileListResponseDTO);

        var profiles = service.findAll();

        Assertions.assertThat(profiles).isNotNull().isEqualTo(profileListResponseDTO);
    }

    @Test
    @DisplayName("save DEVE criar um profile com sucesso")
    void save_CreatedProfile_WhenSuccessful() {
        var profileToSave = profileUtils.profileToSave();
        var profileSaved = profileUtils.profileSaved();
        var request = profileUtils.postRequestDTO();
        var response = profileUtils.postResponseDTO();

        BDDMockito.when(mapper.toProfile(request)).thenReturn(profileToSave);
        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileSaved);
        BDDMockito.when(mapper.toPostResponse(profileSaved)).thenReturn(response);

        var profile = service.save(request);

        Assertions.assertThat(profile).isNotNull().isEqualTo(response).hasNoNullFieldsOrProperties();
    }
}