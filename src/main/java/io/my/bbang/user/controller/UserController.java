package io.my.bbang.user.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.request.UserLoginRequest;
import io.my.bbang.user.payload.response.UserJoinResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@PutMapping("/join")
	public Mono<UserJoinResponse> join(@RequestBody @Valid UserJoinRequest requestBody) {
		log.info("call user Join!!!");
		
		String name = requestBody.getName();
		String loginId = requestBody.getLoginId();
		String password = requestBody.getPassword();
		
		return userService.join(name, loginId, password);
	}
	
	@PostMapping("/login")
	public Mono<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest requestBody) {
		log.info("call user login!!!");
		
		String loginId = requestBody.getLoginId();
		String password = requestBody.getPassword();
		
		return userService.login(loginId, password);
	}

}
