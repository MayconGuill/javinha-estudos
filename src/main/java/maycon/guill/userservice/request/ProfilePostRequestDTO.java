package maycon.guill.userservice.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfilePostRequestDTO(
        @NotBlank(message = "O campo 'name' é obrigatório")
        String name,
        @NotBlank(message = "O campo 'description' é obrigatório")
        String description
) {
}
