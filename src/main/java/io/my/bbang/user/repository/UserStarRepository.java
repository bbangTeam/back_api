package io.my.bbang.user.repository;

import io.my.bbang.user.domain.UserStar;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserStarRepository extends ReactiveCrudRepository<UserStar, String> {
    Mono<Void> deleteByTypeAndUserIdAndParentId(UserStar entity);
}
