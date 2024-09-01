package com.mizitoh.webflux.service;

import com.mizitoh.webflux.entities.User;
import com.mizitoh.webflux.mapper.UserMapper;
import com.mizitoh.webflux.model.request.UserRequest;
import com.mizitoh.webflux.repository.UserRepository;
import com.mizitoh.webflux.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
        UserRequest request = new UserRequest("Nata", "nata@email.com", "123456");
        User entity = User.builder().build();

        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(entity);
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
    }

    @Test
    void testFindById() {
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(User.builder().id("123").build()));

        Mono<User> result = service.findById("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class && Objects.equals(user.getId(), "123"))
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
    }

    @Test
    void testFindAll() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(User.builder().build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UserRequest request = new UserRequest("Nata", "nata@email.com", "123456");
        User entity = User.builder().build();

        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.any(User.class))).thenReturn(entity);
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(entity));
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = service.update("123", request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void testDelete() {
        User entity = User.builder().build();

        Mockito.when(repository.findAndRemove(ArgumentMatchers.anyString())).thenReturn(Mono.just(entity));

        Mono<User> result = service.delete("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();
        Mockito.verify(repository, Mockito.times(1)).findAndRemove(ArgumentMatchers.anyString());
    }

    @Test
    void testHandleNotFound() {
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        try {
            service.findById("123");
        } catch (Exception ex) {
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals(String.format(
                    "Objeto não encontrado, id: %s, tipo: %s", "123", User.class.getSimpleName()
            ), ex.getMessage());
        }

    }
}