package maycon.guill.userservice.commons;

import maycon.guill.userservice.domain.Profile;
import maycon.guill.userservice.request.ProfilePostRequestDTO;
import maycon.guill.userservice.response.ProfileGetResponseDTO;
import maycon.guill.userservice.response.ProfilePostResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class ProfileUtils {
    public List<Profile> profileList() {
        var admin = Profile.builder().id(1L).name("Administrator").description("Admin").build();
        var manager = Profile.builder().id(2L).name("Manager").description("Manager").build();

        return new ArrayList<>((List.of(admin, manager)));
    }

    public List<ProfileGetResponseDTO> profileListResponseDTO() {
        var admin = ProfileGetResponseDTO.builder().id(1L).name("Administrator").description("Admin").build();
        var manager = ProfileGetResponseDTO.builder().id(2L).name("Manager").description("Manager").build();

        return new ArrayList<>((List.of(admin, manager)));
    }

    public Profile profileToSave() {
        return Profile.builder().name("Administrator").description("Admin").build();
    }

    public Profile profileSaved() {
        return Profile.builder().id(99L).name("Administrator").description("Admin").build();
    }

    public ProfilePostRequestDTO postRequestDTO() {
        return ProfilePostRequestDTO.builder().name("Administrator").description("Admin").build();
    }

    public ProfilePostResponseDTO postResponseDTO() {
        return ProfilePostResponseDTO.builder().name("Administrator").description("Admin").build();
    }
}
