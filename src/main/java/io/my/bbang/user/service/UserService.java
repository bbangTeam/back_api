package io.my.bbang.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.security.UserRole;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.payload.response.UserJoinResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.repository.UserHeartRepository;
import io.my.bbang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final UserHeartRepository userHeartRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public Mono<UserJoinResponse> join(String name, String loginId, String password) {
		log.info("join service");
		
		User user = User.newInstance(loginId, passwordEncoder.encode(password));
		user.setName(name);
		user.getRoles().add(UserRole.ROLE_USER);
		
		User saveUser = new User();
		Mono<User> monoEntity = userRepository.save(user);
		
		monoEntity.subscribe(e -> {
			saveUser.setCreateDate(e.getCreateDate());
			saveUser.setLoginId(e.getLoginId());
			saveUser.setId(e.getId());
		});
		
		UserJoinResponse responseBody = new UserJoinResponse();
		responseBody.setId(saveUser.getId());
		responseBody.setLoginId(loginId);
		responseBody.setCreateTime(saveUser.getCreateDate());
		
		return Mono.just(responseBody);
	}
	
	public Mono<UserLoginResponse> login(String loginId, String password) {
		log.info("login service");
		
		UserLoginResponse responseBody = new UserLoginResponse();
			
		Mono<User> testUser = userRepository.findByLoginId(loginId);

		return testUser.map(user -> {
			if (! passwordEncoder.matches(password, user.getPassword())) {
				throw new BbangException("password is wrong!");
			}
			
			String accessToken = jwtUtil.createAccessToken(user.getId());
			
			responseBody.setLoginId(loginId);
			responseBody.setAccessToken(accessToken);
			
			return responseBody;
		});
	}
	
	/**
	 * Security 에서 사용자 정보를 저장하기 위해 사용
	 * @param id
	 * @return
	 */
	public Mono<User> findById(String id) {
		return userRepository.findById(id);
	}

	public Mono<UserHeart> saveUserHeart(UserHeart entity) {
		return userHeartRepository.save(entity);
	}

	public Mono<Void> deleteUserHeart(UserHeart entity) {
		return userHeartRepository.deleteAllByUserIdAndHeartIdAndType(
			entity.getUserId(),
			entity.getHeartId(), 
			entity.getType());
	}

	public Mono<UserHeart> findUserLike(UserHeart entity) {
		return userHeartRepository.findByUserIdAndHeartIdAndType(entity);
	}
	
}
