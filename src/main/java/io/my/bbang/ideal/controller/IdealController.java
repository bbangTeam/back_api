package io.my.bbang.ideal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.ideal.payload.response.IdealContentResponse;
import io.my.bbang.ideal.service.IdealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ideal")
public class IdealController {
	private final IdealService idealService;
	
	@GetMapping("/content")
	public Mono<IdealContentResponse> content() {
		
		return idealService.content();
	}
	

}
