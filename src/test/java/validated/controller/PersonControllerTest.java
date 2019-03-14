package validated.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import validated.dto.PersonRequest;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class PersonControllerTest {
    @Inject
    private EmbeddedServer embeddedServer;


    private HttpResponse<String> actual;
    private BlockingHttpClient httpClient;


    @PostConstruct
    void setupClient() {
        httpClient = RxHttpClient.create(embeddedServer.getURL()).toBlocking();
    }


    private static Stream<Arguments> readRequestValidation() {
        return Stream.of(
                arguments(new PersonRequest("test@example.com", null), true),
                arguments(new PersonRequest(null, "display"), false),
                arguments(new PersonRequest("test@example.com", "display"), false),
                arguments(new PersonRequest(null, null), false)
        );
    }

    @ParameterizedTest
    @MethodSource("readRequestValidation")
    void post_person_validates_incoming_payload(PersonRequest request, boolean isValid) {
        try {
            actual = httpClient.exchange(HttpRequest.POST("/person", request));
            assertTrue(isValid, "Expected request to be not valid but returned without any errors");
        } catch (HttpClientResponseException e) {
            assertFalse(isValid, String.format("Expected request to be valid but returned code %s", e.getStatus()));
        }
    }
}