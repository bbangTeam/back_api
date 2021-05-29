package io.my.bbang.commons.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
	
	@GetMapping("/api/healthcheck")
//	@PreAuthorize("hasRole('USER')")
	public Mono<BbangResponse> healthCheck() {
		log.info("healthcheck!!!");
		
		return Mono.just(new BbangResponse("healthcheck"));
	}

}
