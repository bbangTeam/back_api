package io.my.bbang.user.service;

import java.time.LocalDateTime;

import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.breadstore.repository.StoreRepository;
import io.my.bbang.comment.repository.CommentRepository;
import io.my.bbang.pilgrimage.repository.PilgrimageRepository;
import io.my.bbang.user.domain.*;
import io.my.bbang.user.dto.UserClickType;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.dto.UserStarType;
import io.my.bbang.user.payload.response.MyProfileResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.repository.*;
import org.springframework.stereotype.Service;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final UserStarRepository userStarRepository;
	private final UserClickRepository userClickRepository;
	private final UserHeartRepository userHeartRepository;
	private final UserIdealRepository userIdealRepository;

	private final StoreRepository storeRepository;
	private final CommentRepository commentRepository;
	private final PilgrimageRepository pilgrimageRepository;
	private final BreadstagramRepository breadstagramRepository;

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

	public Mono<UserIdeal> saveUserIdeal(String idealId) {
		return jwtUtil.getMonoUserId().flatMap(userId -> {
			UserIdeal entity = new UserIdeal();
			entity.setIdealId(idealId);
			entity.setUserId(userId);
			return userIdealRepository.save(entity);
		});
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

	public void click(String id, String type) {
		jwtUtil.getMonoUserId().subscribe(userId -> {
			UserClick entity = new UserClick();
			entity.setUserId(userId);
			entity.setType(type);
			entity.setParentId(id);
			userClickRepository.save(entity).subscribe();
		})
		;

		if (UserClickType.BREADSTAGRAM.isEqualsType(type)) {
			breadstagramRepository.findById(id).subscribe(entity -> {
				entity.setClickCount(entity.getClickCount() + 1);
				breadstagramRepository.save(entity).subscribe();
			});
		} else if (UserClickType.PILGRIMAGE.isEqualsType(type)) {
			pilgrimageRepository.findById(id).subscribe(entity -> {
				entity.setClickCount(entity.getClickCount());
				pilgrimageRepository.save(entity).subscribe();
			});
		} else if (UserClickType.STORE.isEqualsType(type)) {
			storeRepository.findById(id).subscribe(entity -> {
				entity.setClickCount(entity.getClickCount() + 1);
				storeRepository.save(entity).subscribe();
			});
		}
	}

	public void postLike(String id, String type) {
		jwtUtil.getMonoUserId().subscribe(userId -> {
			UserHeart entity = new UserHeart();
			entity.setUserId(userId);
			entity.setType(type);
			entity.setParentId(id);
			userHeartRepository.save(entity).subscribe();
		})
		;

		if (UserHeartType.BREADSTAGRAM.isEqualsType(type)) {
			breadstagramRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() + 1);
				breadstagramRepository.save(entity).subscribe();
			});
		} else if (UserHeartType.PILGRIMAGE.isEqualsType(type)) {
			pilgrimageRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() + 1);
				pilgrimageRepository.save(entity).subscribe();
			});
		} else if (UserHeartType.STORE.isEqualsType(type)) {
			storeRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() + 1);
				storeRepository.save(entity).subscribe();
			});
		} else if (UserHeartType.COMMENT.isEqualsType(type)) {
			commentRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() + 1);
				commentRepository.save(entity).subscribe();
			});
		}
	}

	public void deleteLike(String id, String type) {
		jwtUtil.getMonoUserId()
				.subscribe(userId ->
					userHeartRepository
							.deleteByUserIdAndParentIdAndType(userId, id, type).subscribe())
		;

		if (UserHeartType.BREADSTAGRAM.isEqualsType(type)) {
			breadstagramRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() - 1);
				breadstagramRepository.save(entity).subscribe();
			});
		} else if (UserHeartType.PILGRIMAGE.isEqualsType(type)) {
			pilgrimageRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() - 1);
				pilgrimageRepository.save(entity).subscribe();
			});
		} else if (UserHeartType.STORE.isEqualsType(type)) {
			storeRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() - 1);
				storeRepository.save(entity).subscribe();
			});
		} else if (UserHeartType.COMMENT.isEqualsType(type)) {
			commentRepository.findById(id).subscribe(entity -> {
				entity.setLikeCount(entity.getLikeCount() - 1);
				commentRepository.save(entity).subscribe();
			});
		}
	}

	public void postStar(String id, String type, int star) {

		jwtUtil.getMonoUserId().subscribe(userId -> {
			UserStar entity = new UserStar();
			entity.setUserId(userId);
			entity.setType(type);
			entity.setParentId(id);
			entity.setStar(star);
			userStarRepository.save(entity).subscribe();
		});

		if (UserStarType.STORE.isEqualsType(type)) {
			storeRepository.findById(id).subscribe(entity -> {
				entity.setStarCount(entity.getStarCount() + 1);
				entity.setStarSum(entity.getStarSum() + star);
				entity.setStar((double) entity.getStarSum() / entity.getStarCount());
				storeRepository.save(entity).subscribe();
			});
		}
	}

	public void deleteStar(String id, String type, int star) {
		jwtUtil.getMonoUserId().subscribe(userId -> {
			UserStar entity = new UserStar();
			entity.setType(type);
			entity.setUserId(userId);
			entity.setParentId(id);
			entity.setStar(star);
			userStarRepository.deleteByTypeAndUserIdAndParentId(entity).subscribe();
		});

		if (UserStarType.STORE.isEqualsType(type)) {
			storeRepository.findById(id).subscribe(entity -> {
				entity.setStarCount(entity.getStarCount() - 1);
				entity.setStarSum(entity.getStarSum() - star);
				entity.setStar((double) entity.getStarSum() / entity.getStarCount());
				storeRepository.save(entity).subscribe();
			});
		}
	}

}
