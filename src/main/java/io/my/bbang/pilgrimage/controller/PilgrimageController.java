package io.my.bbang.pilgrimage.controller;

import io.my.bbang.pilgrimage.payload.request.PilgrimageWriteRequest;
import io.my.bbang.pilgrimage.payload.response.PilgrimageBoardListResponse;
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
	public Mono<PilgrimageListResponse> list(@RequestParam String id) {
		return pilgrimageService.list(id);
	}

	@GetMapping("/area/list")
	public Mono<PilgrimageAreaListResponse> areaList() {
		return pilgrimageService.areaList();
	}

	@PostMapping("/board")
	public Mono<BbangResponse> writePilgrimage(@RequestBody PilgrimageWriteRequest requestBody) {
		return pilgrimageService.write(requestBody);
	}

	@PostMapping("/visit")
	public Mono<BbangResponse> visit(@RequestParam("id") String id) {
		return pilgrimageService.visit(id);
	}

	@GetMapping("/board/list")
	public Mono<PilgrimageBoardListResponse> boardList(
			@RequestParam("id") String id,
			@RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum) {
		return pilgrimageService.boardList(id, pageNum, pageSize);
	}

}
