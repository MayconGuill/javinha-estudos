package maycon.guill.userservice.response;

import lombok.Builder;

@Builder
public record UserPostResponseDTO(
        String firstName,
        String lastName,
        String email
) {
}
