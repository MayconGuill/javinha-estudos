package maycon.guill.userservice.controllers;

import maycon.guill.userservice.commons.FileUtils;
import maycon.guill.userservice.config.IntegrationTestConfiguration;
import maycon.guill.userservice.response.ProfileGetResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureRestTestClient
@Sql(value = "/sql/clean-database-profiles.sql")
class ProfileControllerIT extends IntegrationTestConfiguration {
//    private static final String URL = "/v1/profiles";
//
//    @Autowired
//    private RestTestClient restTestClient;
//
//    @Autowired
//    private FileUtils fileUtils;
//
//    @Test
//    @Sql(value = "/sql/init_two_profiles.sql")
//    @DisplayName("GET v1/profiles DEVE retornar uma lista de profiles")
//    void findAll_ReturnAllProfiles_WhenSuccessful() {
//        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponseDTO>>() {};
//
//        restTestClient.get()
//                .uri(URL).exchange()
//                .expectStatus().isOk()
//                .expectBody(typeReference)
//                .value(profiles -> {
//                    assertThat(profiles).isNotNull().hasSize(2);
//                    profiles.forEach(profileGetResponseDTO -> assertThat(profileGetResponseDTO).hasNoNullFieldsOrProperties());
//                });
//    }
//
//    @Test
//    @DisplayName("GET v1/profiles DEVE retornar uma lista de profiles")
//    void findAll_ReturnEmptyListProfile_WhenProfileNotExists() {
//        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponseDTO>>() {};
//
//        restTestClient.get()
//                .uri(URL).exchange()
//                .expectStatus().isOk()
//                .expectBody(typeReference)
//                .value(profiles -> {
//                    assertThat(profiles).isNotNull().isEmpty();
//                });
//    }
//
//    @Test
//    @DisplayName("POST v1/profiles DEVE salvar um profile com sucesso")
//    void save_CreatedProfile_WhenSuccessful() throws Exception {
//        var request = fileUtils.readResourceFile("profile/post-request-create-profile-200.json");
//        var response = fileUtils.readResourceFile("profile/post-response-created-profile-201.json");
//
//        restTestClient.post().uri(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(request)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .json(response)
//                .returnResult();
//    }
//
//    @ParameterizedTest
//    @MethodSource("postProfileBadRequestErrors")
//    @DisplayName("POST v1/profiles DEVE retornar um BAD REQUEST SE campos forem inválidos")
//    void save_ReturnBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws Exception {
//        var request = fileUtils.readResourceFile("/profile/%s".formatted(requestFile));
//        var expectedResponse = fileUtils.readResourceFile("/profile/%s".formatted(responseFile));
//
//        var responseBody = restTestClient.post().uri(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(request)
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody(String.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThatJson(responseBody)
//                .whenIgnoringPaths("timestamp")
//                .isEqualTo(expectedResponse);
//    }
//
//    private static Stream<Arguments> postProfileBadRequestErrors() {
//        return Stream.of(
//                Arguments.of("post-request-profile-empty-fields-400.json", "post-response-profile-empty-fields-400.json"),
//                Arguments.of("post-request-profile-blanky-fields-400.json", "post-response-profile-blanky-fields-400.json")
//        );
//    }
}