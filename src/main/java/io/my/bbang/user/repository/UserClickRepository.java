package io.my.bbang.user.repository;

import io.my.bbang.user.domain.UserClick;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClickRepository extends ReactiveCrudRepository<UserClick, String> {
}
