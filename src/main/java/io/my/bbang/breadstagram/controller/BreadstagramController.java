package io.my.bbang.breadstagram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramViewResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.breadstagram.service.BreadstagramService;
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
			@RequestParam(defaultValue = "1", required = false) int pageNum, 
			@RequestParam int pageSize) {
		
		return breadstagramService.list(pageNum, pageSize);
	}
	
	@GetMapping("/view")
	public Mono<BreadstagramViewResponse> view(
			@RequestParam String id) {
		return breadstagramService.view(id);
	}
	
	
	@PutMapping("/write")
	public Mono<BreadstagramWriteResponse> write(@RequestBody BreadstagramWriteRequest requestBody) {
		log.info("/api/breadstagram/write 요청전문: {}", requestBody);
		return breadstagramService.write(requestBody);
	}
	
	
}
