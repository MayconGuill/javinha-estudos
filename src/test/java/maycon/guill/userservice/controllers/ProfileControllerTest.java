package maycon.guill.userservice.controllers;

import maycon.guill.userservice.commons.FileUtils;
import maycon.guill.userservice.commons.ProfileUtils;
import maycon.guill.userservice.domain.Profile;
import maycon.guill.userservice.repositories.ProfileRepository;
import maycon.guill.userservice.repositories.UserProfileRepository;
import maycon.guill.userservice.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProfileController.class)
@ComponentScan(basePackages = {"maycon.guill.userservice"})
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    private ProfileRepository repository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserProfileRepository userProfileRepository;

    private ProfileUtils profileUtils;

    private List<Profile> profileList;

    private static final String URL = "/v1/profiles";

    @BeforeEach
    void init() {
        profileUtils = new ProfileUtils();
        profileList = profileUtils.profileList();
    }

    @Test
    @DisplayName("GET v1/profiles DEVE retornar uma lista de profiles")
    void findAll_ReturnAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var response = fileUtils.readResourceFile("profile/get-profile-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/profiles DEVE salvar um profile com sucesso")
    void save_CreatedProfile_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-create-profile-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-created-profile-201.json");
        var profileToSave = profileUtils.profileToSave();
        var profileSaved = profileUtils.profileSaved();

        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileSaved);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestErrors")
    @DisplayName("POST v1/profiles DEVE retornar uma exception SE campos forem invalidos")
    void save_ReturnBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postProfileBadRequestErrors() {
        var nameError = "O campo 'name' é obrigatório";
        var descriptionError = "O campo 'description' é obrigatório";

        var errors = List.of(nameError, descriptionError);

        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", errors),
                Arguments.of("post-request-profile-blanky-fields-400.json", errors)
        );
    }
}