package io.my.bbang.user.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.user.domain.UserHeart;
import reactor.core.publisher.Mono;

@Repository
public interface UserHeartRepository extends ReactiveCrudRepository<UserHeart, String> {
    Mono<Void> deleteAllByUserIdAndParentIdAndType(String userId, String heartId, String type);
    Mono<UserHeart> findByUserIdAndParentIdAndType(UserHeart entity);
    Mono<Long> countByUserId(String userId);
    Mono<Void> deleteByUserIdAndParentIdAndType(String userId, String parentId, String type);
}
