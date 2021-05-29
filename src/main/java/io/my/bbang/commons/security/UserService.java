package io.my.bbang.commons.security;

import org.springframework.stereotype.Service;

import io.my.bbang.test.entity.TestEntity;
import io.my.bbang.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	private final TestRepository testRepository;
	
	public Mono<TestEntity> findById(String id) {
		return testRepository.findById(id);
	}
	

}
