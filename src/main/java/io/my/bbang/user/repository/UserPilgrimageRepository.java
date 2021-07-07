package io.my.bbang.user.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.user.domain.UserPilgrimage;
import reactor.core.publisher.Mono;

@Repository
public interface UserPilgrimageRepository extends ReactiveCrudRepository<UserPilgrimage, String> {
    Mono<UserPilgrimage> findByUserIdAndPilgrimageId(String userId, String pilgrimageId);
    
}
