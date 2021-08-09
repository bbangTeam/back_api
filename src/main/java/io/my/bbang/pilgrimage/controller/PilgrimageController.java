package io.my.bbang.pilgrimage.controller;

import io.my.bbang.pilgrimage.payload.request.PilgrimageWriteRequest;
import io.my.bbang.pilgrimage.payload.response.PilgrimageBoardList;
import org.springframework.web.bind.annotation.*;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.pilgrimage.payload.response.PilgrimageAreaListResponse;
import io.my.bbang.pilgrimage.payload.response.PilgrimageListResponse;
import io.my.bbang.pilgrimage.service.PilgrimageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pilgrimage")
public class PilgrimageController {
	private final PilgrimageService pilgrimageService;

	@GetMapping("/list")
	public Mono<PilgrimageListResponse> list(
			@RequestParam String id, 
			@RequestParam(required = false, defaultValue = "none") String option
	) {
		log.info("call pilgrimage list!!!");
		
		return pilgrimageService.list(id, option);
	}

	@GetMapping("/area/list")
	public Mono<PilgrimageAreaListResponse> areaList() {
		return pilgrimageService.areaList();
	}

	@PostMapping("/board")
	public Mono<BbangResponse> boardWrite(@RequestBody PilgrimageWriteRequest requestBody) {
		return pilgrimageService.boardWrite(requestBody);
	}

	@GetMapping("/board/list")
	public Mono<PilgrimageBoardList> boardList(
			@RequestParam(defaultValue = "0", required = false) int pageNum,
			@RequestParam(defaultValue = "5", required = false) int pageSize) {
		return pilgrimageService.boardList(pageNum, pageSize);
	}


}
