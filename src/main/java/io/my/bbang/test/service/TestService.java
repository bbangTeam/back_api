package io.my.bbang.test.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.my.bbang.test.entity.TestEntity;
import io.my.bbang.test.repository.TestRepository;
import io.my.bbang.test.vo.response.TestJoinResponse;
import io.my.bbang.test.vo.response.TestLoginResponse;
import io.my.commons.exception.BbangException;
import io.my.commons.security.UserRole;
import io.my.commons.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
	private final JwtUtil jwtUtil;
	private final TestRepository testRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public Mono<TestJoinResponse> join(String name, String loginId, String password) {
		log.info("join service");
		
		TestEntity entity = TestEntity.newInstance(loginId, passwordEncoder.encode(password));
		entity.setName(name);
		entity.getRoles().add(UserRole.ROLE_USER);
		entity.getRoles().add(UserRole.ROLE_ADMIN);
		
		TestEntity saveEntity = new TestEntity();
		Mono<TestEntity> monoEntity = testRepository.save(entity);
		
		monoEntity.subscribe(e -> {
			saveEntity.setCreatedTime(e.getCreatedTime());
			saveEntity.setLoginId(e.getLoginId());
			saveEntity.setId(e.getId());
		});
		
		TestJoinResponse responseBody = new TestJoinResponse();
		responseBody.setId(saveEntity.getId());
		responseBody.setLoginId(loginId);
		responseBody.setCreateTime(saveEntity.getCreatedTime());
		
		return Mono.just(responseBody);
	}
	
	public Mono<TestLoginResponse> login(String loginId, String password) {
		log.info("login service");
		
		TestLoginResponse responseBody = new TestLoginResponse();
			
		Mono<TestEntity> testUser = testRepository.findByLoginId(loginId);

		return testUser.map(user -> {
			if (! passwordEncoder.matches(password, user.getPassword())) {
				throw new BbangException("password is wrong!");
			}
			
			String accessToken = jwtUtil.createAccessToken(user.getId());
			String refreshToken = jwtUtil.createRefreshToken(user.getId());
			
			responseBody.setLoginId(loginId);
			responseBody.setAccessToken(accessToken);
			responseBody.setRefreshToken(refreshToken);
			
			return responseBody;
		});
	}

}
