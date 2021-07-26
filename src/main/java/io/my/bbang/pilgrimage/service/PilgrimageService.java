package io.my.bbang.pilgrimage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.service.StoreService;
import io.my.bbang.code.dto.ParentCode;
import io.my.bbang.code.service.CodeService;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.pilgrimage.domain.Pilgrimage;
import io.my.bbang.pilgrimage.dto.PilgrimageAreaListDto;
import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import io.my.bbang.pilgrimage.payload.response.PilgrimageAreaListResponse;
import io.my.bbang.pilgrimage.payload.response.PilgrimageListResponse;
import io.my.bbang.pilgrimage.repository.PilgrimageRepository;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PilgrimageService {
	private final JwtUtil jwtUtil;

	private final PilgrimageRepository pilgrimageRepository;
	
	private final StoreService storeService;
	private final UserService userService;
	private final CodeService codeService;

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
				return userService.findByUserPilgrimageId(dto.getPilgrimageId());
			}).map(entity -> {
				PilgrimageListDto dto = dtoMap.get(entity.getPilgrimageId());
				dto.setClear(true);

				// 응답 값으로 불필요하므로, null 처리
				dto.setPilgrimageId(null);
				return dto;
			}).collectList().map(list -> {
				list = dtoMap.values().stream().collect(Collectors.toList());
				// 응답 값으로 불필요하므로, null 처리
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
		return storeService.findOneStore(entity.getStoreId());
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

	public Mono<PilgrimageAreaListResponse> areaList() {
		return codeService.findAllByParentCode(ParentCode.PILGRIMAGE_ADDRESS_CODE.getCode())
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

	// public
}
