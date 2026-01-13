package maycon.guill.userservice.response;

import lombok.Builder;

@Builder
public record ProfileGetResponseDTO(
        Long id,
        String name,
        String description
) {
}
