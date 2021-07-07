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
import io.my.bbang.comment.domain.Comment;
import io.my.bbang.comment.dto.CommentType;
import io.my.bbang.comment.service.CommentService;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BreadstagramService {
	private final BreadstagramRepository breadstagramRepository;
	private final CommentService commentService;
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
	
	public Mono<BreadstagramWriteResponse> write(BreadstagramWriteRequest requestBody) {

		String storeId = requestBody.getId();
		String breadName = requestBody.getBreadName();
		String cityName = requestBody.getCityName();
		String content = requestBody.getContent();
		
		List<BreadstagramImageDto> imageDtoList = requestBody.getImageList();

		Breadstagram entity = new Breadstagram();
		entity.setBreadName(breadName);
		entity.setStoreId(storeId);
		entity.setCityName(cityName);
		entity.setImageList(imageDtoList);
		entity.setCreateDate(LocalDateTime.now());

		return breadstagramRepository.save(entity)
				.flatMap(breadstagram -> {
					entity.setId(breadstagram.getId());
					return jwtUtil.getMonoUserId();
				})
				.flatMap(userId -> userService.findById(userId))
				.flatMap(user -> 
					commentService.save(
						Comment.build(entity.getId(), user.getId(), user.getLoginId(), content, CommentType.STORE)))
				.map(comment -> {
					BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();
					responseBody.setId(entity.getId());
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
			if (store.getLike() == null) store.setLike(0);
			store.setLike(like ? store.getLike() + 1 : store.getLike() - 1);
			return storeService.save(store);
		})
		.flatMap(store -> {
			return jwtUtil.getMonoUserId()
			.map(userId -> {
				String storeId = store.getId();
				UserHeart userHeart = UserHeart.build(userId, storeId, UserHeartType.STORE);

				if (like) userService.saveUserHeart(userHeart).subscribe();
				else userService.deleteUserHeart(userHeart).subscribe();

				return new BbangResponse("Success");
			});
		})
		;
	}
		

}
