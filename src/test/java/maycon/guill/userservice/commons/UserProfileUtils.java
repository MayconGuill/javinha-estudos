package maycon.guill.userservice.commons;

import maycon.guill.userservice.domain.Profile;
import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.domain.UserProfile;
import maycon.guill.userservice.response.UserProfileGetResponseDTO;
import maycon.guill.userservice.response.UserProfileUserGetResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class UserProfileUtils {
    public List<UserProfile> userProfileList() {
        var saitama = User.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
        var genus = User.builder().id(2L).firstName("Genus").lastName("Robo Aprendiz").email("genus@gmail.com").build();

        var admin = Profile.builder().id(1L).name("Administrator").description("Admin").build();

        var userProfileSaitama = UserProfile.builder().id(1L).user(saitama).profile(admin).build();
        var userProfileGenus = UserProfile.builder().id(2L).user(genus).profile(admin).build();

        return new ArrayList<>((List.of(userProfileSaitama, userProfileGenus)));
    }

    public List<UserProfileGetResponseDTO> userProfileListResponseDTO() {
        var saitama = UserProfileGetResponseDTO.User.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
        var genus = UserProfileGetResponseDTO.User.builder().id(2L).firstName("Genus").lastName("Robo Aprendiz").email("genus@gmail.com").build();

        var admin = UserProfileGetResponseDTO.Profile.builder().id(1L).name("Administrator").description("Admin").build();

        var userProfileSaitama = UserProfileGetResponseDTO.builder().id(1L).user(saitama).profile(admin).build();
        var userProfileGenus = UserProfileGetResponseDTO.builder().id(2L).user(genus).profile(admin).build();

        return new ArrayList<>((List.of(userProfileSaitama, userProfileGenus)));
    }

    public List<UserProfileUserGetResponseDTO> userProfileUserListResponseDTO() {
        var saitama = UserProfileUserGetResponseDTO.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
        var genus = UserProfileUserGetResponseDTO.builder().id(2L).firstName("Genus").lastName("Robo Aprendiz").email("genus@gmail.com").build();

        return new ArrayList<>((List.of(saitama, genus)));
    }

    public List<User> userList() {
        var saitama = User.builder().id(1L).firstName("Saitama").lastName("Careca de Capa").email("saitama@gmail.com").build();
        var genus = User.builder().id(2L).firstName("Genus").lastName("Robo Aprendiz").email("genus@gmail.com").build();

        return new ArrayList<>((List.of(saitama, genus)));
    }
}
