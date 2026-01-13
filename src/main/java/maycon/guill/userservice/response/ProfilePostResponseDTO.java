package maycon.guill.userservice.response;

import lombok.Builder;

@Builder
public record ProfilePostResponseDTO(
        String name,
        String description
) {
}
