package maycon.guill.userservice.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import maycon.guill.userservice.commons.FileUtils;
import maycon.guill.userservice.config.IntegrationTestConfiguration;
import maycon.guill.userservice.repositories.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerRestAssuredIT extends IntegrationTestConfiguration {
    private static final String URL = "/v1/users";

    @Autowired
    private UserRepository repository;

    @Autowired
    private FileUtils fileUtils;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @ParameterizedTest
    @MethodSource("getUsersListIfNameIsNullOrEmpty")
    @Sql(value = "/sql/init_two_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET v1/users?firstName=? DEVE retornar uma lista de usuários SE firstName for EMPTY ou NULL")
    void findAll_ReturnAllUsers_WhenFirstNameIsEmptyOrNull(String firstName, String fileName) {
        var response = fileUtils.readResourceFile("user/%s".formatted(fileName));

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .queryParam("firstName", firstName)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response));
    }

    @Test
    @Sql(value = "/sql/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET v1/users?firstName=Saitama DEVE retornar uma lista de usuários SE firstName for encontrado")
    void findAll_ReturnAllUsers_WhenFirstNameIsFound() {
        var response = fileUtils.readResourceFile("user/get-user-saitama-name-200.json");
        var firstName = "Saitama";

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .queryParam("firstName", firstName)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response));
    }

    @Test
    @Sql(value = "/sql/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET v1/users?firstName=x DEVE retornar uma lista EMPTY SE firstName não for encontrado")
    void findAll_ReturnAllUsers_WhenFirstNameIsNotFound() {
        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");
        var firstName = "x";

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .queryParam("firstName", firstName)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response));
    }

    @Test
    @Sql(value = "/sql/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET v1/users/{id} DEVE retornar um usuário SE id for encontrado")
    void findById_ReturnUser_WhenIdIsFound() {
        var response = fileUtils.readResourceFile("user/get-user-1-id-200.json");
        var id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response));
    }

    @Test
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST v1/users DEVE salvar um usuário com sucesso")
    void save_CreatedUser_WhenSuccessful() {
        var request = fileUtils.readResourceFile("user/post-request-create-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-created-user-201.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(Matchers.equalTo(response));
    }

    @Test
    @Sql(value = "/sql/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("DELETE v1/users/{id} DEVE remover um usuário com sucesso")
    void delete_RemovedUser_WhenSuccessful() {
        var id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Sql(value = "/sql/init_one_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean-database-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("UPDATE v1/users DEVE atualizar um usuário SE id for encontrado")
    void update_UpdatedUser_WhenIdIsFound() {
        var request = fileUtils.readResourceFile("user/update-request-user-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var user = repository.findAll().getFirst();
        Assertions.assertEquals("Shadow", user.getFirstName());
    }

    private static Stream<Arguments> getUsersListIfNameIsNullOrEmpty() {
        return Stream.of(
                Arguments.of("", "get-user-empty-name-200.json"),
                Arguments.of(" ", "get-user-empty-name-200.json"),
                Arguments.of(null, "get-user-null-name-200.json")
        );
    }
}