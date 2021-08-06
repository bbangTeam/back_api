package io.my.bbang.user.service;

import java.time.LocalDateTime;

import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.comment.repository.CommentRepository;
import io.my.bbang.user.payload.response.MyProfileResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import org.springframework.stereotype.Service;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.domain.UserIdeal;
import io.my.bbang.user.domain.UserPilgrimage;
import io.my.bbang.user.repository.UserHeartRepository;
import io.my.bbang.user.repository.UserIdealRepository;
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
	private final UserIdealRepository userIdealRepository;
	private final UserPilgrimageRepository userPilgrimageRepository;

	private final BreadstagramRepository breadstagramRepository;
	private final CommentRepository commentRepository;

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
							.flatMap(bool -> returnResponse(bool, nickname));
	}

	private Mono<BbangResponse> returnResponse(boolean bool, String nickname) {
		return bool
				? Mono.just(new BbangResponse(11, "Already Exist!"))
				: saveUser(nickname).map(this::returnModifyNicknameResponse);
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

	public Mono<User> saveUser(User user) {
		return userRepository.save(user);
	}

	public Mono<UserLoginResponse> buildUserLoginResponseByUser(User user) {
		UserLoginResponse responseBody = new UserLoginResponse();
		responseBody.setLoginId(user.getEmail());
		responseBody.setAccessToken(jwtUtil.createAccessToken(user.getId()));
		return Mono.just(responseBody);
	}
	
	public Mono<User> findById(String id) {
		return userRepository.findById(id);
	}

	public Mono<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
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
	
	public Mono<UserIdeal> saveUserIdeal(String idealId) {
		return jwtUtil.getMonoUserId().flatMap(userId -> {
			UserIdeal entity = new UserIdeal();
			entity.setIdealId(idealId);
			entity.setUserId(userId);
			return userIdealRepository.save(entity);
		});
	}

	public Mono<UserIdeal> findUserIdealByUserId() {
		return jwtUtil.getMonoUserId().flatMap(userIdealRepository::findByuserId);
	}

	public Mono<MyProfileResponse> getMyProfile() {
		MyProfileResponse responseBody = new MyProfileResponse();
		return jwtUtil.getMonoUserId()
				.flatMap(userRepository::findById)
				.map(user -> {
					responseBody.setUserId(user.getId());
					responseBody.setNickname(user.getNickname());
					responseBody.setProfileImageUrl(user.getImageUrl());
					responseBody.setEmail(user.getEmail());
					return user;
				})
				.flatMap(user -> breadstagramRepository.countAllByUserId(responseBody.getUserId()))
				.flatMap(count -> {
//					responseBody.setPostCount(count);
//					String userId = responseBody.getUserId();
//					return commentRepository.countByUserIdAndType(userId, CommentType.PILGRIMAGE.getValue());
					return Mono.just(0);
				})
				.flatMap(count -> {
					responseBody.setPostCount(responseBody.getPostCount() + count);
					String userId = responseBody.getUserId();
					return commentRepository.countByuserId(userId);
				})
				.flatMap(count -> {
					responseBody.setCommentCount(count);
					String userId = responseBody.getUserId();
					return userHeartRepository.countByUserId(userId);
				})
				.map(count -> {
					responseBody.setLikeCount(count);
					return responseBody;
				})
				;
	}

}
