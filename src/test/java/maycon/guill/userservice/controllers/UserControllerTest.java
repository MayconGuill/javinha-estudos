package maycon.guill.userservice.controllers;

import maycon.guill.userservice.commons.FileUtils;
import maycon.guill.userservice.commons.UserUtils;
import maycon.guill.userservice.domain.User;
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
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(UserController.class)
@ComponentScan(basePackages = {"maycon.guill.userservice"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockitoBean
    private UserRepository repository;

    @MockitoBean
    private ProfileRepository profileRepository;

    @MockitoBean
    private UserProfileRepository userProfileRepository;

    private UserUtils userUtils;

    private List<User> userList;

    private static final String URL = "/v1/users";

    @BeforeEach
    void init() {
        userUtils = new UserUtils();
        userList = userUtils.userList();
    }

    @ParameterizedTest
    @MethodSource("getUsersListIfNameIsNullOrEmpty")
    @DisplayName("GET v1/users?firstName=\" \" DEVE retornar uma lista de usuários SE firstName for EMPTY ou NULL")
    void findAll_ReturnAllUsers_WhenFirstNameIsEmptyOrNull(String firstName, String fileName) throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var response = fileUtils.readResourceFile("user/%s".formatted(fileName));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .param("firstName", firstName)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/paginated DEVE retornar uma lista de usuários paginada")
    void findAll_ReturnListUsers_WhenSuccessful() throws Exception {
        var pageRequest = PageRequest.of(0, userList.size());
        var usersPaginated = new PageImpl<>(userList, pageRequest, userList.size());

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(usersPaginated);

        var response = fileUtils.readResourceFile("user/get-user-paginated-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/paginated")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?firstName=Saitama DEVE retornar uma lista de usuários SE firstName for encontrado")
    void findAll_ReturnAllUsers_WhenFirstNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-saitama-name-200.json");
        var firstName = "Saitama";
        var saitama = userList.stream().filter(user -> user.getFirstName().equals(firstName)).findFirst().orElse(null);
        var list = Collections.singletonList(saitama);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .param("firstName", firstName)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?firstName=x DEVE retornar uma lista EMPTY SE firstName não for encontrado")
    void findAll_ReturnAllUsers_WhenFirstNameIsNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(Collections.emptyList());

        var response = fileUtils.readResourceFile("user/get-user-x-name-404.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .param("firstName", name)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/{id} DEVE retornar um usuário SE id for encontrado")
    void findById_ReturnUser_WhenIdIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-1-id-200.json");
        var id = 1L;
        var userOptional = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userOptional);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/{id} DEVE lançar uma exception SE id não for encontrado")
    void findById_ReturnUser_WhenIdIsNotFound() throws Exception {
        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST v1/users DEVE salvar um usuário com sucesso")
    void save_CreatedUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-create-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-created-user-201.json");
        var userToSave = userUtils.userToSave();
        var userSaved = userUtils.userSaved();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userSaved);

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
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users DEVE retornar uma exception SE campos forem invalidos")
    void save_ReturnBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

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

    @Test
    @DisplayName("DELETE v1/users/{id} DEVE remover um usuário com sucesso")
    void delete_RemovedUser_WhenSuccessful() throws Exception {
        var id = 1L;

        BDDMockito.when(repository.existsById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users/{id} DEVE lançar uma exception SE id não for encontrado")
    void delete_ThrowsUserNotFoundException_WhenIdNotFound() throws Exception {
        var id = 99L;

        BDDMockito.when(repository.existsById(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("UPDATE v1/users DEVE atualizar um usuário SE id for encontrado")
    void update_UpdatedUser_WhenIdIsFound() throws Exception {
        var request = fileUtils.readResourceFile("user/update-request-user-200.json");
        var id = 1L;
        var userOptional = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(userOptional);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("UPDATE v1/users DEVE lançar uma exception SE id não for encontrado")
    void update_ThrowsUserNotFoundException_WhenIdNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/update-request-user-404.json");
        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users DEVE retornar uma exception SE campos forem invalidos")
    void update_ReturnBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
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

    private static Stream<Arguments> postUserBadRequestSource() {
        var messageErrorsRequired = messageErrorsRequired();
        var emailRequiredError = emailInvalidError();

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", messageErrorsRequired),
                Arguments.of("post-request-user-blanky-fields-400.json", messageErrorsRequired),
                Arguments.of("post-request-user-invalid-email-400.json", emailRequiredError)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var messageErrorsRequired = messageErrorsRequired();
        messageErrorsRequired.add("O campo 'id' é obrigatório");
        var emailRequiredError = emailInvalidError();

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", messageErrorsRequired),
                Arguments.of("put-request-user-blanky-fields-400.json", messageErrorsRequired),
                Arguments.of("put-request-user-invalid-email-400.json", emailRequiredError)
        );
    }

    private static Stream<Arguments> getUsersListIfNameIsNullOrEmpty() {
        return Stream.of(
                Arguments.of("", "get-user-empty-name-200.json"),
                Arguments.of(" ", "get-user-empty-name-200.json"),
                Arguments.of(null, "get-user-null-name-200.json")
        );
    }

    private static List<String> emailInvalidError() {
        var emailInvalidError = "Email não é valido";

        return List.of(emailInvalidError);
    }

    private static List<String> messageErrorsRequired() {
        var firstNameError = "O campo 'firstName' é obrigatório";
        var lastNameError = "O campo 'lastName' é obrigatório";
        var emailRequiredError = "O 'email'  é obrigatório";

        return new ArrayList<>(List.of(firstNameError, lastNameError, emailRequiredError));
    }
}