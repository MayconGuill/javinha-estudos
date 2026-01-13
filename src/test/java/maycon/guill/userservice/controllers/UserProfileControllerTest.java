package maycon.guill.userservice.controllers;

import maycon.guill.userservice.commons.FileUtils;
import maycon.guill.userservice.commons.UserProfileUtils;
import maycon.guill.userservice.domain.UserProfile;
import maycon.guill.userservice.repositories.ProfileRepository;
import maycon.guill.userservice.repositories.UserProfileRepository;
import maycon.guill.userservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

@WebMvcTest(UserProfileController.class)
@ComponentScan(basePackages = {"maycon.guill.userservice"})
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockitoBean
    private ProfileRepository profileRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserProfileRepository repository;

    private UserProfileUtils userProfileUtils;

    private List<UserProfile> userProfileList;

    private static final String URL = "/v1/user-profile";

    @BeforeEach
    void init() {
        userProfileUtils = new UserProfileUtils();
        userProfileList = userProfileUtils.userProfileList();
    }

    @Test
    @DisplayName("GET v1/user-profile DEVE retornar uma lista de usuários e seus perfis associados")
    void findAll_ReturnAllUserProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var response = fileUtils.readResourceFile("user-profile/get-user-profile-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/user-profile/profile/{id}/users DEVE retornar uma lista de usuários parametrizado pelo profileId")
    void findByProfileId_ReturnAllUserProfiles_WhenSuccessful() throws Exception {
        var userList = userProfileUtils.userList();
        var profileId = 1L;

        BDDMockito.when(repository.findByUsersProfileId(profileId)).thenReturn(userList);

        var response = fileUtils.readResourceFile("user-profile/get-user-profile-profileId-1-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/profile/{id}/users", profileId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/user-profile/profile/{id}/users DEVE retornar uma lista de usuários parametrizado pelo profileId")
    void findByProfileId_ReturnEmptyListUserProfiles_WhenProfileIdNotFound() throws Exception {
        var profileId = 99L;

        BDDMockito.when(repository.findByUsersProfileId(profileId)).thenReturn(Collections.emptyList());

        var response = fileUtils.readResourceFile("user-profile/get-user-profile-profileId-99-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/profile/{id}/users", profileId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
}