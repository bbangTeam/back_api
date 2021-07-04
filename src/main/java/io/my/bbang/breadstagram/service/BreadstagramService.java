package io.my.bbang.breadstagram.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.my.bbang.breadstagram.domain.Breadstagram;
import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramViewResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.breadstore.service.StoreService;
import io.my.bbang.commons.context.ReactiveJwtContextHolder;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BreadstagramService {
	private final String STORE = "store";

	private final BreadstagramRepository breadstagramRepository;
	private final StoreService storeService;
	private final UserService userService;
	private final JwtUtil jwtUtil;

	public Mono<BreadstagramListResponse> list(int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Direction.DESC, "createDate"));

		return breadstagramRepository.findByIdNotNull(pageable).map(entity -> {
			BreadstagramListDto dto = new BreadstagramListDto();
			dto.setBreadName(entity.getBreadName());
			dto.setId(entity.getId());
			dto.setCityName(entity.getCityName());
			dto.setImageList(entity.getImageList());
			dto.setStoreId(entity.getStoreId());

			return storeService.findOneStore(dto.getStoreId()).map(store -> {
				dto.setBreadStoreName(store.getEntrpNm());
				dto.setLike(store.getLike());
				return dto;
			}).defaultIfEmpty(dto);
		}).flatMap(map -> map).collectList().map(list -> {
			BreadstagramListResponse responseBody = new BreadstagramListResponse();
			responseBody.setResult("Success");
			responseBody.setBreadstagramList(list);
			return responseBody;
		}).switchIfEmpty(Mono.error(new BbangException("Exception")));
	
	}
	
	/**
	 * 삭제된 API
	 * @param id
	 * @return
	 */
	@Deprecated
	public Mono<BreadstagramViewResponse> view(String id) {
		BreadstagramViewResponse responseBody = new BreadstagramViewResponse();

		responseBody.setResult("Success");
		responseBody.setBreadName("소보루");
		responseBody.setCityName("서울");
		responseBody.setNickname("빵터짐");
		responseBody.setStoreName("빵터짐 1호점");
		responseBody.setLike((int)((Math.random()*100000)));

		return breadstagramRepository.findById(id).map(entity -> {
			BreadstagramListDto dto = new BreadstagramListDto();
			responseBody.setBreadName(entity.getBreadName());
			responseBody.setCityName(entity.getCityName());
			return responseBody;
		});
	}
	
	public Mono<BreadstagramWriteResponse> write(BreadstagramWriteRequest requestBody) {

		String storeId = requestBody.getId();
		String storeName = requestBody.getStoreName();
		String breadName = requestBody.getBreadName();
		String cityName = requestBody.getCityName();

		// 댓글에 들어가야함
		String content = requestBody.getContent();
		
		// 이미지에 들어가야함
		List<BreadstagramImageDto> imageList = requestBody.getImageList();

		Breadstagram entity = new Breadstagram();
		entity.setBreadName(breadName);
		entity.setStoreId(storeId);
		entity.setCityName(cityName);
		entity.setImageList(imageList);
		entity.setCreateDate(LocalDateTime.now());

		return breadstagramRepository.save(entity).map(breadstagram -> {
			BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();

			responseBody.setId(breadstagram.getId());
			responseBody.setResult("Success");
			return responseBody;
		}).switchIfEmpty(Mono.error(new BbangException("Exception")));
	}

	/**
	 * @param id
	 * @param like
	 * @return
	 * @throws InterruptedException
	 */
	@Transactional
	public Mono<BbangResponse> like(String id, Boolean like) throws InterruptedException {
		return storeService.findOneStore(id)
		.flatMap(store -> {
			store.setLike(like ? store.getLike() + 1 : store.getLike() - 1);
			return storeService.save(store);
		})
		.flatMap(store -> {
			return ReactiveJwtContextHolder.getContext()
			.flatMap(token -> token.getJwt())
			.map(jwt -> jwtUtil.getUserIdByAccessToken(jwt))
			.map(userId -> {
				String storeId = store.getId();
				UserHeart userHeart = UserHeart.build(userId, storeId, STORE);

				if (like) userService.saveUserHeart(userHeart).subscribe();
				else userService.deleteUserHeart(userHeart).subscribe();

				return new BbangResponse("Success");
			});
		})
		;
	}
		

}
