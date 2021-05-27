package io.my.bbang.test.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.test.entity.TestEntity;
import reactor.core.publisher.Mono;

@Repository
public interface TestRepository extends ReactiveMongoRepository<TestEntity, String> {
	
	Mono<TestEntity> findByLoginId(String loginId);

}
