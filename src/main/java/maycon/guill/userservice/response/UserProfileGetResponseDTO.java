package maycon.guill.userservice.response;

import lombok.Builder;

@Builder
public record UserProfileGetResponseDTO(
        Long id,
        User user,
        Profile profile
) {
    @Builder
    public record User(Long id, String firstName, String lastName, String email) {}
    @Builder
    public record Profile(Long id, String name, String description) {}
}
