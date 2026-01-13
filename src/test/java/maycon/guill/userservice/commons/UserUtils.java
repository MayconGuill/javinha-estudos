package maycon.guill.userservice.commons;

import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.request.UserPostRequestDTO;
import maycon.guill.userservice.request.UserPutRequestDTO;
import maycon.guill.userservice.response.UserGetResponseDTO;
import maycon.guill.userservice.response.UserPostResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    public List<User> userList() {
        var saitama = User.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
        var genus = User.builder().id(2L).firstName("Genus").lastName("Robo Aprendiz").email("genus@gmail.com").build();

        return new ArrayList<>((List.of(saitama, genus)));
    }

    public List<UserGetResponseDTO> userListResponseDTO() {
        var saitama = UserGetResponseDTO.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
        var genus = UserGetResponseDTO.builder().id(2L).firstName("Genus").lastName("Robo Aprendiz").email("genus@gmail.com").build();

        return new ArrayList<>((List.of(saitama, genus)));
    }

    public User userToSave() {
        return User.builder().firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
    }

    public User userSaved() {
        return User.builder().id(99L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
    }

    public UserPostRequestDTO postRequestDTO() {
        return UserPostRequestDTO.builder().firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
    }

    public UserPostResponseDTO postResponseDTO() {
        return UserPostResponseDTO.builder().firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
    }

    public UserPutRequestDTO putRequestDTO() {
        return UserPutRequestDTO.builder().id(1L).firstName("Saitama").lastName("Careca").email("saitama@gmail.com").build();
    }

    public UserGetResponseDTO getResponseDTO() {
        return UserGetResponseDTO.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
    }
}
