package com.mizitoh.webflux.controller.implementacao;

import com.mizitoh.webflux.controller.UserController;
import com.mizitoh.webflux.model.request.UserRequest;
import com.mizitoh.webflux.model.response.UserResponse;
import com.mizitoh.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserControllerImplemantation implements UserController {

    private final UserService userService;
    @Override
    public ResponseEntity<Mono<Void>> save(UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request).then());
        //.then retorna void
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> find(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return null;
    }
}
