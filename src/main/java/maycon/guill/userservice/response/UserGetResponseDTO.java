package maycon.guill.userservice.response;

import lombok.Builder;

@Builder
public record UserGetResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
