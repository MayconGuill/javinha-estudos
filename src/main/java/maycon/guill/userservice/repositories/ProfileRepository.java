package maycon.guill.userservice.repositories;

import maycon.guill.userservice.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
