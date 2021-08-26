package io.my.bbang.breadstagram.service;

import java.time.LocalDateTime;
import java.util.List;

import io.my.bbang.breadstore.repository.StoreRepository;
import io.my.bbang.user.repository.UserHeartRepository;
import io.my.bbang.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import io.my.bbang.breadstagram.domain.Breadstagram;
import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.dto.UserHeartType;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BreadstagramService {
	private final BreadstagramRepository breadstagramRepository;
	private final UserHeartRepository userHeartRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	private final JwtUtil jwtUtil;

	public Mono<BreadstagramListResponse> list(int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Direction.DESC, "createDate"));

		return breadstagramRepository.findByIdNotNull(pageable)
		.flatMap(entity -> {
			BreadstagramListDto dto = entityToDto(entity);
			return setStoreNameAndLikeCount(dto);
		})
		.flatMap(dto -> userRepository.findById(dto.getUserId())
				.map(user -> {
					dto.setNickname(user.getNickname());
					return dto;
				}))
		.collectList()
		.map(this::returnResponse)
		.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)));
	}

	private BreadstagramListDto entityToDto(Breadstagram entity) {
		BreadstagramListDto dto = new BreadstagramListDto();
		dto.setContent(entity.getContent());
		dto.setBreadName(entity.getBreadName());
		dto.setId(entity.getId());
		dto.setCityName(entity.getCityName());
		dto.setImageList(entity.getImageList());
		dto.setStoreId(entity.getStoreId());
		dto.setUserId(entity.getUserId());
		dto.setCreateDate(entity.getCreateDate());

		dto.setCommentCount(entity.getCommentCount());
		dto.setClickCount(entity.getClickCount());
		dto.setLikeCount(entity.getLikeCount());
		return dto;
	}

	private Mono<BreadstagramListDto> setStoreNameAndLikeCount(BreadstagramListDto dto) {
		return storeRepository.findById(dto.getStoreId()).flatMap(store -> {
			dto.setBreadStoreName(store.getEntrpNm());

			UserHeart userHeart = new UserHeart();
			userHeart.setUserId(dto.getUserId());
			userHeart.setType(UserHeartType.BREADSTAGRAM);
			userHeart.setParentId(dto.getId());

			return userHeartRepository.findByUserIdAndParentIdAndType(userHeart)
					.map(heartEntity -> true)
					.switchIfEmpty(Mono.just(false))
					.flatMap(bool -> {
						dto.setLike(bool);
						return Mono.just(dto);
					});
		});
	}

	private BreadstagramListResponse returnResponse(List<BreadstagramListDto> list) {
		BreadstagramListResponse responseBody = new BreadstagramListResponse();
		responseBody.setResult("Success");
		responseBody.setBreadstagramList(list);
		return responseBody;
	}
	
	public Mono<BreadstagramWriteResponse> write(BreadstagramWriteRequest requestBody) {

		return jwtUtil.getMonoUserId().map(userId -> requestToEntity(requestBody, userId))
				.flatMap(breadstagramRepository::save)
				.map(this::returnResponse)
				.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)));
	}

	private Breadstagram requestToEntity(BreadstagramWriteRequest requestBody, String userId) {
		String storeId = requestBody.getId();
		String breadName = requestBody.getBreadName();
		String cityName = requestBody.getCityName();
		String content = requestBody.getContent();

		Breadstagram entity = new Breadstagram();

		entity.setUserId(userId);
		entity.setBreadName(breadName);
		entity.setStoreId(storeId);
		entity.setCityName(cityName);
		entity.setContent(content);
		entity.setImageList(requestBody.getImageList());
		entity.setCreateDate(LocalDateTime.now());

		entity.setCommentCount(0L);
		entity.setClickCount(0L);
		entity.setLikeCount(0L);
		return entity;
	}

	private BreadstagramWriteResponse returnResponse(Breadstagram entity) {
		BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();
		responseBody.setId(entity.getId());
		responseBody.setResult("Success");
		return responseBody;
	}

		

}
