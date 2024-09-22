package com.mizitoh.webflux.controller;

import com.mizitoh.webflux.entities.User;
import com.mizitoh.webflux.mapper.UserMapper;
import com.mizitoh.webflux.model.request.UserRequest;
import com.mizitoh.webflux.model.response.UserResponse;
import com.mizitoh.webflux.service.UserService;
import com.mizitoh.webflux.service.exception.ObjectNotFoundException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplemantationTest {

    public static final String NAME = "mi";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    private final User entity = User.builder().build();

    private final String id = "123456";
    private final String email = "mi@emallgeal.com";
    private final String senha = "asdferfs";


    @Test
    @DisplayName("Testa endpoint save com sucesso")
    void testSaveSucesso() {
        UserRequest request = new UserRequest(NAME, email, senha);

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(entity));

        webTestClient.post().uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        verify(service).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Testa endpoint save com badrequest")
    void testSaveBasRequest() {
        UserRequest request = new UserRequest(NAME + " ", email, senha);

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(entity));

        webTestClient.post().uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/user")
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Erro de Validação");
    }

    @Test
    @DisplayName("FindbyId com sucesso")
    void findByIdSucesso() {
        final var userResponse = new UserResponse(id, NAME, email, senha);
        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/user/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);
    }

    @Test
    @DisplayName("FindbyId not found")
    void findByIdSemSucesso() {
        String id = "";
        final var userResponse = new UserResponse(id, NAME, email, senha);
        when(service.findById(anyString())).thenThrow(ObjectNotFoundException.class);
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/user/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found");
    }

    @Test
    void findAll() {
        final var userResponse = new UserResponse(id, NAME, email, senha);
        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class);
        
        verify(service).findAll();
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("update com sucesso")
    void update() {
        final var userResponse = new UserResponse(id, NAME, email, senha);
        UserRequest request = new UserRequest(NAME, email, senha);

        when(service.update(anyString(), any(UserRequest.class))).thenReturn(Mono.just(entity));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.patch().uri("/user/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);

        verify(service).update(anyString(), any(UserRequest.class));
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    void delete() {
        when(service.delete(anyString())).thenReturn(Mono.just(entity));

        webTestClient.delete().uri("/user/" + id)
                .exchange()
                .expectStatus().isOk();

        verify(service).delete(anyString());
    }

    @Test
    @DisplayName("delete not found")
    void deleteSemSucesso() {
        String id = "52000";
        when(service.delete(anyString())).thenThrow(ObjectNotFoundException.class);

        webTestClient.delete().uri("/user/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found");

        verify(service).delete(anyString());
    }
}