package io.my.bbang.user.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.user.domain.UserIdeal;
import reactor.core.publisher.Mono;

@Repository
public interface UserIdealRepository extends ReactiveCrudRepository<UserIdeal, String> {
    Mono<UserIdeal> findByuserId(String userId);
    
}
