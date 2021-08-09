package io.my.bbang.breadstagram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.breadstagram.service.BreadstagramService;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/breadstagram")
public class BreadstagramController {
	private final BreadstagramService breadstagramService;

	@GetMapping("/list")
	public Mono<BreadstagramListResponse> list(
			@RequestParam(defaultValue = "0", required = false) int pageNum,
			@RequestParam(defaultValue = "5", required = false) int pageSize) {
		
		return breadstagramService.list(pageNum, pageSize);
	}
	
	@PostMapping("/write")
	public Mono<BreadstagramWriteResponse> write(@RequestBody BreadstagramWriteRequest requestBody) {
		log.info("/api/breadstagram/write 요청전문: {}", requestBody);
		return breadstagramService.write(requestBody);
	}

	@RequestMapping(value="/like", method = {RequestMethod.POST, RequestMethod.DELETE})
	public Mono<BbangResponse> like(
		@RequestParam String id, 
		@RequestParam Boolean like) {
		return breadstagramService.like(id, like);
	}
	
	
}
