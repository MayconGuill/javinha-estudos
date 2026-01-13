package maycon.guill.userservice.response;

import lombok.Builder;

@Builder
public record UserProfileUserGetResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
