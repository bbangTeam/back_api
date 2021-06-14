package io.my.bbang.pilgrimage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	

}
