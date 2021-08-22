package io.my.bbang.breadstore.service;

import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.breadstore.payload.resposne.StoreListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.repository.StoreRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StoreService {
	
	private final StoreRepository storeRepository;
	private final BreadstagramRepository breadstagramRepository;

	public Mono<StoreListResponse> list (double xposLo, double yposLa, int minDistance, int maxDistance) {
		return storeRepository.findByLocation(xposLo, yposLa, minDistance, maxDistance)
			.map(store -> {
				StoreListResponse.StoreList dto = new StoreListResponse.StoreList();
				dto.setId(store.getId());
				dto.setLongitude(store.getXposLo());
				dto.setLatitude(store.getYposLa());
				dto.setStoreName(store.getEntrpNm());
				dto.setImageUrl(store.getNaverThumbUrl());
				dto.setOpenHour(store.getBusinessHours());
				dto.setTel(store.getTelNo());
				dto.setLoadAddr(store.getLoadAddr());
				dto.setStar(store.getStar() > 0 ? store.getStar() : 0.0);
				return dto;
			})
			.flatMap(dto -> breadstagramRepository.countAllByStoreId(dto.getId())
				.map(count -> {
					dto.setReviewCount(count != null ? count : 0);
					return dto;
				})
			).collectList()
			.map(list -> {
				StoreListResponse responseBody = new StoreListResponse();
				responseBody.setStoreList(list);
				return responseBody;
			})
			;
	}

	public Mono<Store> findOneStore(String id) {
		return storeRepository.findById(id);
	}

	public Mono<Store> save(Store store) {
		return storeRepository.save(store);
	}

}
