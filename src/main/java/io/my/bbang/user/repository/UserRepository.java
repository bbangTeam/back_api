package io.my.bbang.user.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.user.domain.User;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
	Mono<User> findByLoginId(String loginId);
	Mono<User> findByNickname(String nickname);
}
