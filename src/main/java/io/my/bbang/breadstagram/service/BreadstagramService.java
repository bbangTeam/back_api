package io.my.bbang.breadstagram.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
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
		Sort sort = Sort.by(Direction.DESC, "modifyDate");
		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

		return breadstagramRepository.findByIdNotNull(pageable)
		.flatMap(entity -> {
			BreadstagramListDto dto = entityToDto(entity);
			return setAboutStore(dto);
		})
		.flatMap(dto -> userRepository.findById(dto.getUserId())
				.map(user -> {
					dto.setNickname(user.getNickname());
					return dto;
				})
				.switchIfEmpty(Mono.just(dto))
		)
		.switchIfEmpty(Mono::just)
		.collectList()
		.map(list -> {
			list.sort(Comparator.comparing(BreadstagramListDto::getModifyDate).reversed());
			return returnResponse(list);
		})
		.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)));
	}

	private BreadstagramListDto entityToDto(Breadstagram entity) {
		BreadstagramListDto dto = new BreadstagramListDto();
		dto.setContent(entity.getContent());
		dto.setBreadNameList(entity.getBreadNameList());
		dto.setId(entity.getId());
		dto.setCityName(entity.getCityName());
		dto.setImageList(entity.getImageList());
		dto.setStoreId(entity.getStoreId());
		dto.setUserId(entity.getUserId());
		dto.setModifyDate(entity.getModifyDate());
		dto.setCreateDate(entity.getCreateDate());

		dto.setCommentCount(entity.getCommentCount());
		dto.setClickCount(entity.getClickCount());
		dto.setLikeCount(entity.getLikeCount());
		return dto;
	}

	private Mono<BreadstagramListDto> setAboutStore(BreadstagramListDto dto) {
		return storeRepository.findById(dto.getStoreId()).flatMap(store -> {
			dto.setBreadStoreName(store.getEntrpNm());
			dto.setStar(store.getStar());

			return userHeartRepository.findByUserIdAndParentIdAndType(
					dto.getUserId(), dto.getId(), UserHeartType.BREADSTAGRAM.getValue()
			)
					.map(heartEntity -> true)
					.defaultIfEmpty(false)
					.flatMap(bool -> {
						dto.setLike(bool);
						return Mono.just(dto);
					})
					;
		}).switchIfEmpty(Mono.just(dto));
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
				.map(entity -> {
					plusStoreReviewCount(entity);
					return returnResponse(entity);
				})
				.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)));
	}

	private Breadstagram requestToEntity(BreadstagramWriteRequest requestBody, String userId) {
		List<String> breadNameList = requestBody.getBreadNameList();
		List<BreadstagramImageDto> imageList = requestBody.getImageList();
		String storeId = requestBody.getId();
		String cityName = requestBody.getCityName();
		String content = requestBody.getContent();

		Breadstagram entity = new Breadstagram();

		entity.setUserId(userId);
		entity.setBreadNameList(breadNameList);
		entity.setStoreId(storeId);
		entity.setCityName(cityName);
		entity.setContent(content);
		entity.setImageList(imageList);
		entity.setCreateDate(LocalDateTime.now());

		return entity;
	}

	private void plusStoreReviewCount(Breadstagram entity) {
		storeRepository.findById(entity.getStoreId()).subscribe(store -> {
			store.setReviewCount(store.getReviewCount() + 1);
			storeRepository.save(store).subscribe();
		});
	}

	private BreadstagramWriteResponse returnResponse(Breadstagram entity) {
		BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();
		responseBody.setId(entity.getId());
		responseBody.setResult("Success");
		return responseBody;
	}


		

}
