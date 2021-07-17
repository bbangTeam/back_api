package io.my.bbang.ideal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.ideal.payload.request.IdealSelectedRequest;
import io.my.bbang.ideal.payload.response.IdealContentResponse;
import io.my.bbang.ideal.payload.response.IdealRankResponse;
import io.my.bbang.ideal.service.IdealService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ideal")
public class IdealController {
	private final IdealService idealService;

	@GetMapping("/done")
	public Mono<BbangResponse> done() {
		return idealService.done();
	}
	
	@GetMapping("/content")
	public Mono<IdealContentResponse> content() {
		
		return idealService.content();
	}

	@GetMapping("/rank")
	public Mono<IdealRankResponse> rank() {
		return idealService.rank();
	}

	@PostMapping("/selected")
	public Mono<BbangResponse> selected(@RequestBody IdealSelectedRequest requestBody) {
		String id = requestBody.getId();
		return idealService.selected(id);
	}
	

}
