package maycon.guill.userservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserPostRequestDTO(
        @NotBlank(message = "O campo 'firstName' é obrigatório")
        String firstName,
        @NotBlank(message = "O campo 'lastName' é obrigatório")
        String lastName,
        @NotBlank(message = "O 'email'  é obrigatório")
        @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email não é valido")
        String email
) {
}
