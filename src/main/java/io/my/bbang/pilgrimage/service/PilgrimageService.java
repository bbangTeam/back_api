package io.my.bbang.pilgrimage.service;

import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.service.StoreService;
import io.my.bbang.code.domain.Code;
import io.my.bbang.code.dto.ParentCode;
import io.my.bbang.code.service.CodeService;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.pilgrimage.dto.PilgrimageAreaListDto;
import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import io.my.bbang.pilgrimage.payload.response.PilgrimageAreaListResponse;
import io.my.bbang.pilgrimage.payload.response.PilgrimageListResponse;
import io.my.bbang.pilgrimage.repository.PilgrimageRepository;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
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
			return pilgrimageRepository.findAllByPilgrimageAddressId(id);
		})
		.map(flux -> {
			PilgrimageListDto dto = new PilgrimageListDto();
			return flux.flatMap(entity -> {

				if (option.equals("all")) {
					dto.setBreadName(entity.getBreadName());
				}

				dto.setPilgrimageId(entity.getId());
				return storeService.findOneStore(entity.getStoreId());
			})
			.flatMap(entity -> {
				dto.setId(entity.getId());
				dto.setStoreName(entity.getEntrpNm());
				dto.setLatitude(entity.getXposLo());
				dto.setLongitude(entity.getYposLa());
				
				if (option.equals("all")) {
					dto.setImageUrl(entity.getNaverThumbUrl());
					dto.setOpeningHours(entity.getBusinessHours());
				}

				return userService.findByUserPilgrimageId(dto.getPilgrimageId());
			}).map(entity -> {
				if (entity == null) {
					dto.setIsClear(Boolean.FALSE);
				} else {
					dto.setIsClear(Boolean.TRUE);
				}
				// 응답 값으로 불필요하므로, null 처리
				dto.setPilgrimageId(null);
				return dto;
			}).collectList().map(list -> {

				// 응답 값으로 불필요하므로, null 처리
				responseBody.setUserId(null);
				responseBody.setStoreList(list);
				responseBody.setResult("Success");
				return responseBody;
			})
			;
		}).flatMap(map -> map);
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
}
