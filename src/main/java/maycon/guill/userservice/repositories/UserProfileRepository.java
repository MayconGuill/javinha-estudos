package maycon.guill.userservice.repositories;

import maycon.guill.userservice.domain.User;
import maycon.guill.userservice.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p ORDER BY up.id ASC")
        // @EntityGraph(value = "UserProfile.fullDetails")
        // @EntityGraph(attributePaths = {"user", "profile"})
    List<UserProfile> findAll();

    @Query("SELECT up.user FROM UserProfile up WHERE up.profile.id = ?1")
    List<User> findByUsersProfileId(Long id);
}
