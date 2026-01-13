package maycon.guill.userservice.mappers;

import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.domain.UserProfile;
import maycon.guill.userservice.response.UserProfileGetResponseDTO;
import maycon.guill.userservice.response.UserProfileUserGetResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    List<UserProfileGetResponseDTO> toUserProfileGetResponseDTO(List<UserProfile> userProfiles);
    List<UserProfileUserGetResponseDTO> toUserProfileUserGetResponseDTO(List<User> users);
}
