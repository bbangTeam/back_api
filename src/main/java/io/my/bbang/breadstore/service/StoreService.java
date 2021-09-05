package io.my.bbang.breadstore.service;

import io.my.bbang.breadstore.domain.StoreBread;
import io.my.bbang.breadstore.payload.request.StoreMenuPatchRequest;
import io.my.bbang.breadstore.payload.request.StoreMenuPostRequest;
import io.my.bbang.breadstore.payload.resposne.StoreBreadListResponse;
import io.my.bbang.breadstore.payload.resposne.StoreListResponse;
import io.my.bbang.breadstore.repository.StoreBreadRepository;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.DateUtil;
import io.my.bbang.commons.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.repository.StoreRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
	private final JwtUtil jwtUtil;
	private final DateUtil dateUtil;
	private final StoreRepository storeRepository;
	private final StoreBreadRepository storeBreadRepository;

	public Mono<StoreListResponse> list (double xposLo, double yposLa, int minDistance, int maxDistance) {
		return storeRepository.findByLocation(xposLo, yposLa, minDistance, maxDistance)
			.map(store -> {
				StoreListResponse.StoreList dto = new StoreListResponse.StoreList();
				dto.setId(store.getId());
				dto.setStar(store.getStar());
				dto.setLikeCount(store.getLikeCount());
				dto.setLongitude(store.getXposLo());
				dto.setLatitude(store.getYposLa());
				dto.setStoreName(store.getEntrpNm());
				dto.setImageUrl(store.getNaverThumbUrl());
				dto.setOpenHour(store.getBusinessHours());
				dto.setTel(store.getTelNo());
				dto.setLoadAddr(store.getLoadAddr());
				dto.setReviewCount(store.getReviewCount());
				return dto;
			})
			.collectList()
			.map(list -> {
				StoreListResponse responseBody = new StoreListResponse();
				responseBody.setStoreList(list);
				return responseBody;
			})
			;
	}

	public Mono<StoreBreadListResponse> stroeBreadList(String id) {
		return storeBreadRepository.findByStoreId(id).flatMap(entity -> {
			StoreBreadListResponse.BreadList bread = new StoreBreadListResponse.BreadList();
			bread.setId(entity.getId());
			bread.setName(entity.getName());
			bread.setPrice(entity.getPrice());
			bread.setModifyDate(entity.getModifyDate());
			return Mono.just(bread);
		})
		.switchIfEmpty(Mono.just(new StoreBreadListResponse.BreadList()))
		.collectList()
		.map(list -> {
			StoreBreadListResponse responseBody = new StoreBreadListResponse();
			responseBody.setBreadList(list);
			return responseBody;
		})
		.switchIfEmpty(Mono.just(new StoreBreadListResponse()));
	}

	public Mono<BbangResponse> postStoreBread(StoreMenuPostRequest requestBody) {
		return jwtUtil.getMonoUserId().map(userId -> {
			StoreBread entity = new StoreBread();

			List<StoreBread.ModifyUserList> userList = new ArrayList<>();
			StoreBread.ModifyUserList user = new StoreBread.ModifyUserList();
			user.setModifyDate(LocalDateTime.now());
			user.setUserId(userId);

			userList.add(user);

			entity.setUserList(userList);
			entity.setBakeTimeList(
					dateUtil.stringListToLocalDateTimeList(
							requestBody.getBakeTimeList()));

			entity.setName(requestBody.getName());
			entity.setPrice(requestBody.getPrice());
			entity.setStoreId(requestBody.getStoreId());
			return storeBreadRepository.save(entity)
					.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)))
			;
		})
		.flatMap(e -> Mono.just(new BbangResponse()));
	}

	public Mono<BbangResponse> patchStoreBread(StoreMenuPatchRequest requestBody) {
		return jwtUtil.getMonoUserId().map(userId ->
			storeBreadRepository.findByStoreId(requestBody.getStoreId()).flatMap(entity -> {
				requestBody.getBakeTimeList().forEach(time ->
						entity.getBakeTimeList().add(
								dateUtil.stringToLocalDateTime(time)));

				StoreBread.ModifyUserList user = new StoreBread.ModifyUserList();
				user.setModifyDate(LocalDateTime.now());
				user.setUserId(userId);
				entity.getUserList().add(user);
				entity.setPrice(requestBody.getPrice());

				return storeBreadRepository.save(entity)
						.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)));
			})
		)
		.flatMap(e -> Mono.just(new BbangResponse()))
		;
	}

}
