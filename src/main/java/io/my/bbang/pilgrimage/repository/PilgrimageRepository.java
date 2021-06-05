package io.my.bbang.pilgrimage.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.user.domain.User;

// TODO 제네릭 타입 변경 ( 도메인을 만들지 않았기 때문에, 일단 User로 생성 )
@Repository
public interface PilgrimageRepository extends ReactiveCrudRepository<User, String>{
}
