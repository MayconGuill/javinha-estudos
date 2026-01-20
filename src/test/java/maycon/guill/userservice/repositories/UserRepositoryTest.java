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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserUtils.class)
class UserRepositoryTest extends IntegrationTestConfiguration {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserUtils utils;

    @Test
    @DisplayName("save cadastra um usuário")
    void save_CreatedUser_WhenSuccessful() {
        var userToSave = utils.userToSave();
        var userSaved = repository.save(userToSave);

        Assertions.assertThat(userSaved).hasNoNullFieldsOrProperties();
        Assertions.assertThat(userSaved.getId()).isNotNull().isPositive();
    }
}