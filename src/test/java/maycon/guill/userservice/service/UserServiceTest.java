package maycon.guill.userservice.service;

import maycon.guill.userservice.commons.UserUtils;
import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.exceptions.EmailBadRequestException;
import maycon.guill.userservice.exceptions.UserNotFoundException;
import maycon.guill.userservice.mappers.UserMapper;
import maycon.guill.userservice.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    private UserUtils userUtils;

    private List<User> userList;

    @BeforeEach
    void init() {
        userUtils = new UserUtils();
        userList = userUtils.userList();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("findAll DEVE retornar todos os usuários SE firstName for NULL ou EMPTY")
    void findAll_ReturnsAllUsers_WhenFirstNameIsNullOrEmpty(String firstName) {
        var userListResponseDTO = userUtils.userListResponseDTO();

        BDDMockito.when(repository.findAll()).thenReturn(userList);
        BDDMockito.when(mapper.toGetResponse(userList)).thenReturn(userListResponseDTO);

        var users = service.findAll(firstName);

        Assertions.assertThat(users).isNotNull().isEqualTo(userListResponseDTO);

        BDDMockito.verify(repository).findAll();
        BDDMockito.verify(repository, BDDMockito.never()).findByFirstNameIgnoreCase(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("findAll DEVE retornar todos os usuários SE nome for encontrado")
    void findAll_ReturnsAllUsers_WhenFirstNameIsFound() {
        var user = userList.getFirst();
        var singletonList = singletonList(user);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(singletonList);

        var response = userUtils.getResponseDTO();
        var singletonListResponseDTO = singletonList(response);

        BDDMockito.when(mapper.toGetResponse(singletonList)).thenReturn(singletonListResponseDTO);

        var userListFound = service.findAll(user.getFirstName());

        Assertions.assertThat(userListFound).isNotNull().isEqualTo(singletonListResponseDTO);

        BDDMockito.verify(repository).findByFirstNameIgnoreCase(user.getFirstName());
        BDDMockito.verify(repository, BDDMockito.never()).findAll();
    }

    @Test
    @DisplayName("findAllPaginated DEVE retornar todos os usuários por paginação")
    void findAllPaginated_ReturnPageUsers_WhenSuccessful() {
        var user = userList.getFirst();
        var pageRequest = PageRequest.of(0, 10);
        var userPaginated = new PageImpl<>(List.of(user), pageRequest, 1);

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(userPaginated);
        BDDMockito.when(mapper.toGetResponse(user)).thenReturn(userUtils.getResponseDTO());

        var paginated = service.findAllPaginated(pageRequest);

        Assertions.assertThat(paginated).isNotNull().contains(userUtils.getResponseDTO());

        BDDMockito.verify(repository).findAll(BDDMockito.any(Pageable.class));
        BDDMockito.verify(repository, BDDMockito.never()).findAll();
    }

    @Test
    @DisplayName("findAll DEVE retornar uma lista EMPTY SE nome não for encontrado")
    void findAll_ReturnsListEmpty_WhenFirstNameIsNotFound() {
        var user = userList.getFirst();

        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(emptyList());

        var userListFound = service.findAll(user.getFirstName());

        Assertions.assertThat(userListFound).isNotNull().isEmpty();

        BDDMockito.verify(repository).findByFirstNameIgnoreCase(user.getFirstName());
        BDDMockito.verify(repository, BDDMockito.never()).findAll();
        BDDMockito.verify(mapper).toGetResponse(emptyList());
    }

    @Test
    @DisplayName("findById DEVE retornar um usuário SE id for encontrado")
    void findById_ReturnUser_WhenIdIsFound() {
        var user = userList.getFirst();
        var response = userUtils.getResponseDTO();

        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(mapper.toGetResponse(user)).thenReturn(response);

        var userFound = service.findById(user.getId());

        Assertions.assertThat(userFound).isNotNull().isEqualTo(response);
    }

    @Test
    @DisplayName("findById DEVE lançar uma exception SE id não for encontrado")
    void findById_ThrowsResponseUserNotFoundException_WhenIdIsNotFound() {
        var user = userList.getFirst();

        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(user.getId()))
                .isInstanceOf(UserNotFoundException.class);

        BDDMockito.verify(mapper, BDDMockito.never()).toGetResponse(user);
    }

    @Test
    @DisplayName("save DEVE criar um usuário com sucesso")
    void save_CreatedUser_WhenSuccessful() {
        var userToSave = userUtils.userToSave();
        var userSaved = userUtils.userSaved();
        var request = userUtils.postRequestDTO();
        var response = userUtils.postResponseDTO();

        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());
        BDDMockito.when(mapper.toUser(request)).thenReturn(userToSave);
        BDDMockito.when(repository.save(userToSave)).thenReturn(userSaved);
        BDDMockito.when(mapper.toPostResponse(userSaved)).thenReturn(response);

        var user = service.save(request);

        Assertions.assertThat(user).isNotNull().isEqualTo(response).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("save DEVE lançar uma exception SE email já existir")
    void save_ThrowsResponseEmailBadRequestException_WhenEmailExists() {
        var userToSave = userUtils.userToSave();
        var request = userUtils.postRequestDTO();

        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.of(userToSave));

        Assertions.assertThatException().isThrownBy(() -> service.save(request))
                .isInstanceOf(EmailBadRequestException.class);

        BDDMockito.verify(mapper, BDDMockito.never()).toUser(request);
        BDDMockito.verify(repository, BDDMockito.never()).save(userToSave);
    }

    @Test
    @DisplayName("delete DEVE remover um usuário com sucesso")
    void delete_RemovedUser_WhenSuccessful() {
        var user = userList.getFirst();

        BDDMockito.when(repository.existsById(user.getId())).thenReturn(true);
        BDDMockito.doNothing().when(repository).deleteById(user.getId());

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(user.getId()));

        BDDMockito.verify(repository).deleteById(user.getId());
    }

    @Test
    @DisplayName("delete DEVE lançar uma exception SE id não for encontrado")
    void delete_ThrowsReponseUserNotFoundException_WhenIdIsNotFound() {
        var user = userList.getFirst();

        BDDMockito.when(repository.existsById(user.getId())).thenReturn(false);

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(user.getId()))
                .isInstanceOf(UserNotFoundException.class);

        BDDMockito.verify(repository, BDDMockito.never()).deleteById(user.getId());
    }

    @Test
    @DisplayName("update DEVE atualizar um usuário SE id for encontrado")
    void update_UpdatedUser_WhenIdIsFound() {
        var user = userList.getFirst();

        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(repository.findByEmailAndIdNot(user.getEmail(), user.getId())).thenReturn(Optional.empty());

        var request = userUtils.putRequestDTO();

        BDDMockito.doNothing().when(mapper).updateUser(request, user);
        BDDMockito.when(repository.save(user)).thenReturn(user);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(request));

        BDDMockito.verify(mapper).updateUser(request, user);
        BDDMockito.verify(repository).save(user);
    }

    @Test
    @DisplayName("update DEVE lançar uma exception SE id e email não pertencerem ao mesmo usuário")
    void update_ThrowNewEmailBadRequestException_WhenEmailAndIdNotMatch() {
        var userFirst = userList.getFirst();
        var userLast = userList.getLast();

        BDDMockito.when(repository.findById(userFirst.getId())).thenReturn(Optional.of(userFirst));
        BDDMockito.when(repository.findByEmailAndIdNot(userFirst.getEmail(), userFirst.getId())).thenReturn(Optional.of(userLast));

        var request = userUtils.putRequestDTO();

        Assertions.assertThatException().isThrownBy(() -> service.update(request))
                .isInstanceOf(EmailBadRequestException.class);

        BDDMockito.verify(mapper, BDDMockito.never()).updateUser(request, userFirst);
        BDDMockito.verify(repository, BDDMockito.never()).save(userFirst);
    }

    @Test
    @DisplayName("update DEVE lançar uma exception SE id não for encontrado")
    void update_ThrowsResponseUserNotFoundException_WhenIdNotIsFound() {
        var user = userList.getFirst();
        var request = userUtils.putRequestDTO();

        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(request))
                .isInstanceOf(UserNotFoundException.class);

        BDDMockito.verify(mapper, BDDMockito.never()).updateUser(request, user);
        BDDMockito.verify(repository, BDDMockito.never()).save(user);
    }
}