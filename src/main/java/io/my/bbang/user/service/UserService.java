package io.my.bbang.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.domain.UserPilgrimage;
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

	public Mono<BbangResponse> checkNickname(String nickname) {
		return userRepository.findByNickname(nickname)
							.hasElement()
							.map(this::returnCheckNicknameResponse);
	}

	private BbangResponse returnCheckNicknameResponse(boolean bool) {
		return bool ? new BbangResponse(11, "Alreay Exist!")
					: new BbangResponse("Success");
	}

	public Mono<BbangResponse> modifyNickname(String nickname) {
		return userRepository.findByNickname(nickname)
							.hasElement()
							.flatMap(bool -> {
								return bool 
									? Mono.just(new BbangResponse(11, "Already Exist!"))
									: saveUser(nickname).map(this::returnModifyNicknameResponse);
							});
	}

	private Mono<Boolean> saveUser(String nickname) {
		return jwtUtil.getMonoUserId()
		.flatMap(this::findById)
		.map(user -> {
			user.setNickname(nickname);
			user.setModifyDate(LocalDateTime.now());
			return user;
		})
		.flatMap(userRepository::save)
		.hasElement();
	}

	private BbangResponse returnModifyNicknameResponse(Boolean bool) {
		return bool ? new BbangResponse("Success")
					: new BbangResponse(12, "nickname change fail!");
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
