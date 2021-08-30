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

	public void postStar(String parentId, String type, int star) {
		jwtUtil.getMonoUserId().subscribe(userId -> {
			if (UserStarType.STORE.isEqualsType(type)) {
				storeStar(userId, parentId, star);
			} else if (UserStarType.PILGRIMAGE.isEqualsType(type)) {
				pilgrimageStar(userId, parentId, star);
			}
		});
	}

	private void storeStar(String userId, String storeId, int star) {
		userStarRepository.findByTypeAndUserIdAndParentId(UserStarType.STORE.getValue(), userId, storeId).subscribe(entity -> {
			if (entity == null) saveStoreStar(userId, storeId, star);
			else updateStoreStar(entity, star);
		});
	}

	private void saveStoreStar(String userId, String storeId, int star) {
		UserStar userStar = new UserStar();
		userStar.setUserId(userId);
		userStar.setStar(star);
		userStar.setParentId(storeId);
		userStar.setType(UserStarType.STORE.getValue());

		userStarRepository.save(userStar).subscribe();
		storeRepository.findById(storeId).subscribe(store -> {
			store.setStarCount(store.getStarCount() + 1);
			store.setStarSum(store.getStarSum() + star);
			store.setStar(
					setStar(store.getStarSum(), store.getStarCount())
			);
			storeRepository.save(store).subscribe();
		});
	}

	private void updateStoreStar(UserStar userStar, int star) {
		storeRepository.findById(userStar.getParentId()).subscribe(store -> {
			store.setStarSum(store.getStarSum() - userStar.getStar() + star);
			store.setStar(setStar(store.getStarSum(), store.getStarCount()));

			userStar.setStar(star);
			userStarRepository.save(userStar).subscribe();
			storeRepository.save(store).subscribe();
		});
	}

	private void pilgrimageStar(String userId, String pilgrimageId, int star) {
		userStarRepository.findByTypeAndUserIdAndParentId(UserStarType.PILGRIMAGE.getValue(), userId, pilgrimageId).subscribe(entity -> {
			if (entity == null) savePilgrimageStar(userId, pilgrimageId, star);
			else updatePilgrimageStar(entity, star);
		});
	}

	private void savePilgrimageStar(String userId, String pilgrimageId, int star) {
		UserStar userStar = new UserStar();
		userStar.setUserId(userId);
		userStar.setParentId(pilgrimageId);
		userStar.setStar(star);

		userStarRepository.save(userStar).subscribe();
		pilgrimageRepository.findById(pilgrimageId).subscribe(entity -> {
			entity.setStarCount(entity.getStarCount() + 1);
			entity.setStarSum(entity.getStarSum() + star);
			entity.setStar(
					setStar(entity.getStarSum(), entity.getStarCount())
			);
			pilgrimageRepository.save(entity);
		});
	}

	private void updatePilgrimageStar(UserStar userStar, int star) {
		pilgrimageRepository.findById(userStar.getParentId()).subscribe(pilgrimage -> {
			pilgrimage.setStarSum(pilgrimage.getStarSum() - userStar.getStar() + star);
			pilgrimage.setStar(setStar(pilgrimage.getStarSum(), pilgrimage.getStarCount()));

			userStar.setStar(star);
			pilgrimageRepository.save(pilgrimage).subscribe();
			userStarRepository.save(userStar).subscribe();
		});
	}

	private double setStar(long starSum, long starCount) {
		return ((double) starSum / starCount) / 2;
	}

	public void deleteStar(String parentId, String type) {
		jwtUtil.getMonoUserId().subscribe(userId -> {
			if (UserStarType.STORE.isEqualsType(type)) {
				deleteStoreStar(userId, parentId);
			} else if (UserStarType.PILGRIMAGE.isEqualsType(type)) {
				deletePilgrimageStar(userId, parentId);
			}
		});
	}

	private void deleteStoreStar(String userId, String storeId) {
		userStarRepository.findByTypeAndUserIdAndParentId(UserStarType.STORE.getValue(), userId, storeId).subscribe(userStar -> {
			int star = userStar.getStar();
			userStarRepository.deleteById(userStar.getId()).subscribe();
			storeRepository.findById(storeId).subscribe(store -> {
				store.setStarSum(store.getStarSum() - star);
				store.setStarCount(store.getStarCount() - 1);
				store.setStar(setStar(store.getStarSum(), store.getStarCount()));
				storeRepository.save(store).subscribe();
			});
		});
	}

	private void deletePilgrimageStar(String userId, String pilgrimageId) {
		userStarRepository.findByTypeAndUserIdAndParentId(UserStarType.PILGRIMAGE.getValue(), userId, pilgrimageId).subscribe(userStar -> {
			int star = userStar.getStar();
			userStarRepository.deleteById(userStar.getId()).subscribe();
			pilgrimageRepository.findById(pilgrimageId).subscribe(pilgrimage -> {
				pilgrimage.setStarSum(pilgrimage.getStarSum() - star);
				pilgrimage.setStarCount(pilgrimage.getStarCount() - 1);
				pilgrimage.setStar(setStar(pilgrimage.getStarSum(), pilgrimage.getStarCount()));
				pilgrimageRepository.save(pilgrimage).subscribe();
			});
		});
	}

}
