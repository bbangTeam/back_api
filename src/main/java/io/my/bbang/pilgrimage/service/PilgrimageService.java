package io.my.bbang.pilgrimage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.my.bbang.breadstore.repository.StoreRepository;
import io.my.bbang.code.repository.CodeRepository;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.pilgrimage.domain.PilgrimageBoard;
import io.my.bbang.pilgrimage.payload.request.PilgrimageWriteRequest;
import io.my.bbang.pilgrimage.payload.response.PilgrimageBoardListResponse;
import io.my.bbang.pilgrimage.repository.PilgrimageBoardRepository;
import io.my.bbang.user.domain.UserPilgrimage;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.repository.UserHeartRepository;
import io.my.bbang.user.repository.UserPilgrimageRepository;
import io.my.bbang.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.code.dto.ParentCode;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.pilgrimage.domain.Pilgrimage;
import io.my.bbang.pilgrimage.dto.PilgrimageAreaListDto;
import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import io.my.bbang.pilgrimage.payload.response.PilgrimageAreaListResponse;
import io.my.bbang.pilgrimage.payload.response.PilgrimageListResponse;
import io.my.bbang.pilgrimage.repository.PilgrimageRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PilgrimageService {
	private final JwtUtil jwtUtil;

	private final UserRepository userRepository;
	private final CodeRepository codeRepository;
	private final StoreRepository storeRepository;
	private final UserHeartRepository userHeartRepository;
	private final PilgrimageRepository pilgrimageRepository;
	private final UserPilgrimageRepository userPilgrimageRepository;
	private final PilgrimageBoardRepository pilgrimageBoardRepository;

	public Mono<PilgrimageListResponse> list(String id) {
		PilgrimageListResponse responseBody = new PilgrimageListResponse();
		return jwtUtil.getMonoUserId()
				.flatMap(userId -> {
					responseBody.setUserId(userId);
					Map<String, PilgrimageListDto> dtoMap = new HashMap<>();
					return pilgrimageRepository.findAllByPilgrimageAreaId(id)
							.flatMap(entity -> findStore(dtoMap, entity))
							.flatMap(entity -> {
								PilgrimageListDto dto = returnPilgrimageListDto(dtoMap, entity);
								return findByUserPilgrimageId(dto.getPilgrimageId());
							})
							.flatMap(entity -> {
								PilgrimageListDto dto = dtoMap.get(entity.getPilgrimageId());
								dto.setClear(true);

								// 응답 값으로 불필요하므로, null 처리
								dto.setPilgrimageId(null);
								return userHeartRepository.findByUserIdAndParentIdAndType(
										responseBody.getUserId(), dto.getId(), UserHeartType.PILGRIMAGE.getValue()
								)
								.map(e -> {
									dto.setLike(true);
									return dto;
								})
								.switchIfEmpty(Mono.just(dto));
							})
							.collectList()
							.map(list -> {
								list = new ArrayList<>(dtoMap.values());
								responseBody.setUserId(null);
								responseBody.setStoreList(list);
								responseBody.setResult("Success");
								return responseBody;
							})
							;
				});
	}

	private Mono<Store> findStore(Map<String, PilgrimageListDto> dtoMap, Pilgrimage entity) {
		PilgrimageListDto dto = new PilgrimageListDto();
		dtoMap.put(entity.getStoreId(), dto);
		dto.setPilgrimageId(entity.getId());
		dto.setLikeCount(entity.getLikeCount());
		dto.setStar(entity.getStar());
		dto.setReviewCount(entity.getReviewCount());
		return storeRepository.findById(entity.getStoreId());
	}

	private PilgrimageListDto returnPilgrimageListDto(Map<String, PilgrimageListDto> dtoMap, Store entity) {
		PilgrimageListDto dto = dtoMap.get(entity.getId());
		dto.setId(entity.getId());
		dto.setImageUrl(entity.getNaverThumbUrl());
		dto.setStoreName(entity.getEntrpNm());
		dto.setLatitude(entity.getYposLa());
		dto.setLongitude(entity.getXposLo());
		dto.setBreadName(entity.getReprsntMenuNm());
		dto.setOpeningHours(entity.getBusinessHours());
		dto.setBakeTimeList(entity.getBakeTimeList());

		dtoMap.remove(entity.getId());
		dtoMap.put(dto.getPilgrimageId(), dto);

		return dto;
	}

	public Mono<UserPilgrimage> findByUserPilgrimageId(String pilgrimageId) {
		return jwtUtil.getMonoUserId()
				.flatMap(userId -> userPilgrimageRepository.findByUserIdAndPilgrimageId(userId, pilgrimageId));
	}

	public Mono<PilgrimageAreaListResponse> areaList() {
		return codeRepository.findAllByCodes(ParentCode.PILGRIMAGE_ADDRESS_CODE.getCode())
		.collectList()
		.map(codeList -> {
			PilgrimageAreaListResponse responseBody = new PilgrimageAreaListResponse();

			codeList.forEach(code -> {
				PilgrimageAreaListDto dto = new PilgrimageAreaListDto();
				dto.setId(code.getId());
				dto.setName(code.getContent());
				responseBody.getAreaList().add(dto);
			});
			responseBody.setResult("Success");
			return responseBody;
		})
		;
	}

	 public void write(PilgrimageWriteRequest requestBody) {
		PilgrimageBoard entity = new PilgrimageBoard();

		jwtUtil.getMonoUserId().subscribe(userId -> {
			String storeId = requestBody.getStoreId();
			entity.setUserId(userId);
			entity.setTitle(requestBody.getTitle());
			entity.setContent(requestBody.getContent());
			entity.setStoreId(storeId);
			entity.setCommentCount(0);

			pilgrimageBoardRepository.save(entity).subscribe();
			pilgrimageRepository.findByStoreId(storeId).subscribe(pilgrimage -> {
				pilgrimage.setReviewCount(pilgrimage.getReviewCount() + 1);
				pilgrimageRepository.save(pilgrimage).subscribe();
			});
		});
	 }

	 public void visit(String id) {
		jwtUtil.getMonoUserId().subscribe(userId -> {
			UserPilgrimage entity = new UserPilgrimage();
			entity.setPilgrimageId(id);
			entity.setUserId(userId);
			userPilgrimageRepository.save(entity).subscribe();
		});
	 }

	 public Mono<PilgrimageBoardListResponse> boardList(String id, int pageNum, int pageSize) {
		 Sort sort = Sort.by(Sort.Direction.DESC, "modifyDate");
		 Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

		return pilgrimageBoardRepository.findByStoreId(id, pageable).flatMap(entity -> {
			PilgrimageBoardListResponse.Board board = new PilgrimageBoardListResponse.Board();
			board.setTitle(entity.getTitle());
			board.setCommentCount(entity.getCommentCount());
			board.setId(entity.getId());
			board.setCreateDate(entity.getCreateDate());
			board.setModifyDate(entity.getModifyDate());
			board.setStoreId(entity.getStoreId());
			board.setClickCount(entity.getClickCount());

			return userRepository.findById(entity.getUserId()).map(user -> {
				board.setNickname(user.getNickname());
				return board;
			}).switchIfEmpty(Mono.just(board));
		})
		.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.REQUEST_EXCEPTION)))
		.flatMap(board -> storeRepository.findById(board.getStoreId())
			.flatMap(store -> {
				board.setStoreName(store.getEntrpNm());
				return Mono.just(board);
			})
			.switchIfEmpty(Mono.just(board))
		)
		.collectList().map(list -> {
			PilgrimageBoardListResponse responseBody = new PilgrimageBoardListResponse();
			responseBody.setBoardList(list);
			return responseBody;
		})
		.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.REQUEST_EXCEPTION)));
	 }

}
