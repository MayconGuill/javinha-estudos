package maycon.guill.userservice.repositories;

import maycon.guill.userservice.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserUtils.class)
class UserRepositoryTest {
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
        Assertions.assertThat(userSaved.getId()).isEqualTo(1L);
    }
}