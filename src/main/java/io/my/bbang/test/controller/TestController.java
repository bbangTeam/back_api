package io.my.bbang.test.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.test.service.TestService;
import io.my.bbang.test.vo.request.TestJoinRequest;
import io.my.bbang.test.vo.request.TestLoginRequest;
import io.my.bbang.test.vo.response.TestJoinResponse;
import io.my.bbang.test.vo.response.TestLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
	private final TestService testService;
	
	@PutMapping("/join")
	public Mono<TestJoinResponse> join(@RequestBody @Valid TestJoinRequest requestBody) {
		log.info("call test join!!!");
		
		String name = requestBody.getName();
		String loginId = requestBody.getLoginId();
		String password = requestBody.getPassword();
		
		return testService.join(name, loginId, password);
	}

	@PostMapping("/login")
	public Mono<TestLoginResponse> login(@RequestBody @Valid TestLoginRequest requestBody) {
		log.info("call test login!!!");
		
		String loginId = requestBody.getLoginId();
		String password = requestBody.getPassword();
		
		return testService.login(loginId, password);
	}
}
