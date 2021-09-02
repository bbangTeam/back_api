package io.my.bbang.breadstore.controller;

import io.my.bbang.breadstore.payload.request.StoreMenuPatchRequest;
import io.my.bbang.breadstore.payload.request.StoreMenuPostRequest;
import io.my.bbang.breadstore.payload.resposne.StoreBreadListResponse;
import io.my.bbang.breadstore.payload.resposne.StoreListResponse;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.my.bbang.breadstore.service.StoreService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
	private final StoreService storeService;
	
	@GetMapping(path="/list")
	public Mono<StoreListResponse> stroeList(
			@RequestParam("longitude") double longitude,
			@RequestParam("latitude") double latitude,
			@RequestParam(required = false, name = "minDistance", defaultValue = "0") int minDistance,
			@RequestParam(required = false, name = "maxDistance", defaultValue = "1000") int maxDistance) {
		return storeService.list(longitude, latitude, minDistance, maxDistance);
	}

	@GetMapping(path="/bread/list")
	public Mono<StoreBreadListResponse> stroeBreadList(@RequestParam String id) {
		return storeService.stroeBreadList(id);
	}

	@PostMapping("/bread")
	public Mono<BbangResponse> postStoreBread(
			@RequestBody StoreMenuPostRequest requestBody) {
		storeService.postStoreBread(requestBody);
		return Mono.just(new BbangResponse());
	}

	@PatchMapping("/bread")
	public Mono<BbangResponse> patchStoreBread(
			@RequestBody StoreMenuPatchRequest requestBody) {
		storeService.patchStoreBread(requestBody);
		return Mono.just(new BbangResponse());
	}
}
