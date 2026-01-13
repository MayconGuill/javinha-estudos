package maycon.guill.userservice.mappers;

import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.request.UserPostRequestDTO;
import maycon.guill.userservice.request.UserPutRequestDTO;
import maycon.guill.userservice.response.UserGetResponseDTO;
import maycon.guill.userservice.response.UserPostResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toUser(UserPostRequestDTO request);

    UserPostResponseDTO toPostResponse(User user);

    List<UserGetResponseDTO> toGetResponse(List<User> usuarios);

    UserGetResponseDTO toGetResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserPutRequestDTO request, @MappingTarget User user);
}
