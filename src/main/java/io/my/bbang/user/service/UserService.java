package io.my.bbang.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.security.UserRole;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.domain.UserPilgrimage;
import io.my.bbang.user.payload.response.UserJoinResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.repository.UserHeartRepository;
import io.my.bbang.user.repository.UserPilgrimageRepository;
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
	private final UserPilgrimageRepository userPilgrimageRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public Mono<UserJoinResponse> join(String name, String loginId, String password) {
		log.info("join service");
		
		User user = User.newInstance(loginId, passwordEncoder.encode(password));
		user.setName(name);
		user.getRoles().add(UserRole.ROLE_USER);
		
		return userRepository.save(user).map(this::returnJoinResponse);
	}

	private UserJoinResponse returnJoinResponse(User entity) {
		UserJoinResponse responseBody = new UserJoinResponse();
		responseBody.setId(entity.getId());
		responseBody.setLoginId(entity.getLoginId());
		responseBody.setCreateTime(entity.getCreateDate());
		return responseBody;
	}
	
	public Mono<UserLoginResponse> login(String loginId, String password) {
		log.info("login service");
		
		return userRepository.findByLoginId(loginId)
		.map(entity -> checkPassword(entity, password))
		.map(this::returnLoginResponse);
	}

	private User checkPassword(User entity, String password) {
		if (! passwordEncoder.matches(password, entity.getPassword())) {
			throw new BbangException(ExceptionTypes.AUTH_EXCEPTION);
		}
		return entity;
	}

	private UserLoginResponse returnLoginResponse(User entity) {
		UserLoginResponse responseBody = new UserLoginResponse();
		String accessToken = jwtUtil.createAccessToken(entity.getId());
		
		responseBody.setLoginId(entity.getLoginId());
		responseBody.setAccessToken(accessToken);
		
		return responseBody;
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

	public Mono<UserPilgrimage> findByUserPilgrimageId(String pilgrimageId) {
		return jwtUtil.getMonoUserId()
		.flatMap(userId -> userPilgrimageRepository.findByUserIdAndPilgrimageId(userId, pilgrimageId));
	}

	public Mono<UserPilgrimage> saveUserPilgrimage(String pilgrimageId) {
		UserPilgrimage entity = new UserPilgrimage();
		entity.setPilgrimageId(pilgrimageId);

		return jwtUtil.getMonoUserId()
		.flatMap(userId -> {
			entity.setUserId(userId);
			return userPilgrimageRepository.save(entity);
		});
	}
	
}
