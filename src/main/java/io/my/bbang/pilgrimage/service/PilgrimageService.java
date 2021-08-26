package io.my.bbang.pilgrimage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.my.bbang.breadstore.repository.StoreRepository;
import io.my.bbang.code.repository.CodeRepository;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.pilgrimage.domain.PilgrimageBoard;
import io.my.bbang.pilgrimage.payload.request.PilgrimageWriteRequest;
import io.my.bbang.pilgrimage.payload.response.PilgrimageBoardList;
import io.my.bbang.pilgrimage.repository.PilgrimageBoardRepository;
import io.my.bbang.user.domain.UserHeart;
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

	private final CodeRepository codeRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final UserHeartRepository userHeartRepository;
	private final PilgrimageRepository pilgrimageRepository;
	private final UserPilgrimageRepository userPilgrimageRepository;
	private final PilgrimageBoardRepository pilgrimageBoardRepository;

	public Mono<PilgrimageListResponse> list(String id, String option) {
		PilgrimageListResponse responseBody = new PilgrimageListResponse();

		return jwtUtil.getMonoUserId()
		.map(userId -> {
			responseBody.setUserId(userId);
			return pilgrimageRepository.findAllByPilgrimageAreaId(id);
		})
		.map(flux -> {
			Map<String, PilgrimageListDto> dtoMap = new HashMap<>();
			return flux.flatMap(entity -> findStore(dtoMap, entity))
			.flatMap(entity -> {
				PilgrimageListDto dto = returnPilgrimageListDto(dtoMap, entity, option);
				return findByUserPilgrimageId(dto.getPilgrimageId());
			}).map(entity -> {
				PilgrimageListDto dto = dtoMap.get(entity.getPilgrimageId());
				dto.setClear(true);

				// 응답 값으로 불필요하므로, null 처리
				dto.setPilgrimageId(null);
				return dto;
			})
			.flatMap(dto -> {
				UserHeart entity = new UserHeart();
				entity.setUserId(responseBody.getUserId());
				entity.setParentId(dto.getId());
				entity.setType(UserHeartType.PILGRIMAGE.getValue());
				return userHeartRepository.findByUserIdAndParentIdAndType(entity)
						.map(e -> true)
						.defaultIfEmpty(false)
						.flatMap(bool -> {
							dto.setLike(bool);
							return Mono.just(dto);
						});
			}).collectList().map(list -> {
				list = new ArrayList<>(dtoMap.values());
				responseBody.setUserId(null);
				responseBody.setStoreList(list);
				responseBody.setResult("Success");
				return responseBody;
			})
			;
		}).flatMap(map -> map);
	}

	private Mono<Store> findStore(Map<String, PilgrimageListDto> dtoMap, Pilgrimage entity) {
		PilgrimageListDto dto = new PilgrimageListDto();
		dtoMap.put(entity.getStoreId(), dto);
		dto.setPilgrimageId(entity.getId());
		dto.setCommentCount(entity.getCommentCount());
		return storeRepository.findById(entity.getStoreId());
	}

	private PilgrimageListDto returnPilgrimageListDto(Map<String, PilgrimageListDto> dtoMap, Store entity, String option) {
		PilgrimageListDto dto = dtoMap.get(entity.getId());
		dto.setId(entity.getId());
		dto.setStoreName(entity.getEntrpNm());
		dto.setLatitude(entity.getYposLa());
		dto.setLongitude(entity.getXposLo());
		
		if (option.equals("all")) {
			dto.setImageUrl(entity.getNaverThumbUrl());
			dto.setBreadName(entity.getReprsntMenuNm());
			dto.setOpeningHours(entity.getBusinessHours());
		}

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

	 public Mono<BbangResponse> boardWrite(PilgrimageWriteRequest requestBody) {
		PilgrimageBoard entity = new PilgrimageBoard();
		entity.setStoreId(requestBody.getStoreName());
		entity.setContent(requestBody.getContent());
		entity.setStoreId(requestBody.getStoreId());
		entity.setStoreName(requestBody.getStoreName());

		return jwtUtil.getMonoUserId().map(userId -> {
			entity.setUserId(userId);
			return entity;
		})
		.flatMap(pilgrimageBoardRepository::save)
		.map(e -> new BbangResponse());
	 }

	 public Mono<PilgrimageBoardList> boardList(int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createDate"));

		return pilgrimageBoardRepository.findByIdNotNull(pageable).map(entity -> {
			PilgrimageBoardList.Board board = new PilgrimageBoardList.Board();
			board.setContent(entity.getContent());
			board.setStoreName(entity.getStoreName());
			board.setUserId(entity.getUserId());
			board.setCreateDate(entity.getCreateDate());
			return board;
		}).flatMap(board -> {
			String userId = board.getUserId();
			return userRepository.findById(userId).map(user -> {
				board.setNickname(user.getNickname());
				return board;
			});
		}).collectList().map(list -> {
			PilgrimageBoardList responseBody = new PilgrimageBoardList();
			responseBody.setBoardList(list);
			return responseBody;
		})
		;
	 }
}
