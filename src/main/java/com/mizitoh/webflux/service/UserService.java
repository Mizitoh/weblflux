package com.mizitoh.webflux.service;

import com.mizitoh.webflux.entities.User;
import com.mizitoh.webflux.mapper.UserMapper;
import com.mizitoh.webflux.model.request.UserRequest;
import com.mizitoh.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public Mono<User> save(final UserRequest request) {
         return userRepository.save(userMapper.toEntity(request));
    }
}
