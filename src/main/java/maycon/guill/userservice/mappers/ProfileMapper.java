package maycon.guill.userservice.mappers;

import maycon.guill.userservice.domain.Profile;
import maycon.guill.userservice.request.ProfilePostRequestDTO;
import maycon.guill.userservice.response.ProfileGetResponseDTO;
import maycon.guill.userservice.response.ProfilePostResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    Profile toProfile(ProfilePostRequestDTO request);

    ProfilePostResponseDTO toPostResponse(Profile profile);

    List<ProfileGetResponseDTO> toGetResponse(List<Profile> profiles);
}
