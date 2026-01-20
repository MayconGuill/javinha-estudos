package maycon.guill.userservice.repositories;

import maycon.guill.userservice.commons.UserUtils;
import maycon.guill.userservice.config.IntegrationTestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserUtils.class)
class UserProfileRepositoryTest extends IntegrationTestConfiguration {
    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("findByProfileId retorna todos os usuários SE profileId for encontrado")
    @Sql("/sql/init_user_profile_2_users_1_profile.sql")
    void findByProfileId_ReturnsAllUserByProfileId_WhenIdIsFound() {
        var profileId = 1L;
        var userProfiles = repository.findByUsersProfileId(profileId);

        Assertions.assertThat(userProfiles).isNotEmpty().hasSize(2).doesNotContainNull();

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }
}