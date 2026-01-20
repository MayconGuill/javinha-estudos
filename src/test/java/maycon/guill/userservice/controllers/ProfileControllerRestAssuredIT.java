package maycon.guill.userservice.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import maycon.guill.userservice.commons.FileUtils;
import maycon.guill.userservice.config.IntegrationTestConfiguration;
import org.hamcrest.Matchers;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/sql/clean-database-profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProfileControllerRestAssuredIT extends IntegrationTestConfiguration {
    private static final String URL = "/v1/profiles";

    @Autowired
    private FileUtils fileUtils;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @Sql(value = "/sql/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("GET v1/profiles DEVE retornar uma lista de profiles")
    void findAll_ReturnAllProfiles_WhenSuccessful() {
        var response = fileUtils.readResourceFile("profile/get-profile-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/profiles DEVE retornar uma lista vazia de profiles")
    void findAll_ReturnEmptyListProfile_WhenProfileNotExists() {
        var response = fileUtils.readResourceFile("profile/get-profile-empty-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/profiles DEVE salvar um profile com sucesso")
    void save_CreatedProfile_WhenSuccessful() {
        var request = fileUtils.readResourceFile("profile/post-request-create-profile-200.json");
        var expectedResponse = fileUtils.readResourceFile("profile/post-response-created-profile-201.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(Matchers.equalTo(expectedResponse));
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestErrors")
    @DisplayName("POST v1/profiles DEVE retornar um BAD REQUEST SE campos forem inválidos")
    void save_ReturnBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("/profile/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("/profile/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> postProfileBadRequestErrors() {
        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", "post-response-profile-empty-fields-400.json"),
                Arguments.of("post-request-profile-blanky-fields-400.json", "post-response-profile-blanky-fields-400.json")
        );
    }
}