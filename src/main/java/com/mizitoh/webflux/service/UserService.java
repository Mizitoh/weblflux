package com.mizitoh.webflux.service;

import com.mizitoh.webflux.entities.User;
import com.mizitoh.webflux.mapper.UserMapper;
import com.mizitoh.webflux.model.request.UserRequest;
import com.mizitoh.webflux.repository.UserRepository;
import com.mizitoh.webflux.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public Mono<User> save(final UserRequest request) {
         return userRepository.save(userMapper.toEntity(request));
    }

    public Mono<User> findById(final String id) {
        return handleNotFound(userRepository.findById(id), id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request) {
        return userRepository.findById(id).map(entity -> userMapper.toEntity(request, entity)).flatMap(userRepository::save);
    }

    public Mono<User> delete(final String id) {
        return handleNotFound(userRepository.findAndRemove(id), id);
    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id){
        return mono.switchIfEmpty(Mono.error(
                new ObjectNotFoundException(
                        String.format(
                                "Objeto não encontrado, id: %s, tipo: %s", id, User.class.getSimpleName()
                        )
                )
        ));
    }
}
