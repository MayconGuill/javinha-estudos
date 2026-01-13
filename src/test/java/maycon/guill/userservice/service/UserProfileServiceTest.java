package maycon.guill.userservice.service;

import maycon.guill.userservice.commons.UserProfileUtils;
import maycon.guill.userservice.domain.UserProfile;
import maycon.guill.userservice.mappers.UserProfileMapper;
import maycon.guill.userservice.repositories.UserProfileRepository;
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
class UserProfileServiceTest {
    @InjectMocks
    private UserProfileService service;

    @Mock
    private UserProfileRepository repository;

    @Mock
    private UserProfileMapper mapper;

    private UserProfileUtils userProfileUtils;

    private List<UserProfile> userProfileList;

    @BeforeEach
    void init() {
        userProfileUtils = new UserProfileUtils();
        userProfileList = userProfileUtils.userProfileList();
    }

    @Test
    @DisplayName("findAll DEVE retornar todos os usuários e seus perfis associados")
    void findAll_ReturnsAllUserProfiles_WhenSuccessful() {
        var userProfileListGetResponse = userProfileUtils.userProfileListResponseDTO();

        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);
        BDDMockito.when(mapper.toUserProfileGetResponseDTO(userProfileList)).thenReturn(userProfileListGetResponse);

        var userProfiles = service.findAll();

        Assertions.assertThat(userProfiles).isNotNull().isEqualTo(userProfileListGetResponse);
    }

    @Test
    @DisplayName("findByProfileId DEVE retornar todos os usuários parametrizado pelo profileId")
    void findByProfileId_ReturnsAllUsers_WhenProfileIdIsFound() {
        var userProfileUserListGetResponse = userProfileUtils.userProfileUserListResponseDTO();
        var userList = userProfileUtils.userList();
        var profileId = 1L;

        BDDMockito.when(repository.findByUsersProfileId(profileId)).thenReturn(userList);
        BDDMockito.when(mapper.toUserProfileUserGetResponseDTO(userList)).thenReturn(userProfileUserListGetResponse);

        var usersByProfileId = service.findByUsersProfileId(profileId);

        Assertions.assertThat(usersByProfileId).isNotNull().isEqualTo(userProfileUserListGetResponse);
    }
}