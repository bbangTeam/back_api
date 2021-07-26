package io.my.bbang.user.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.request.UserLoginRequest;
import io.my.bbang.user.payload.response.UserJoinResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
	private final UserLoginService userLoginService;
	
	@PostMapping("/join")
	public Mono<UserJoinResponse> join(@RequestBody @Valid UserJoinRequest requestBody) {
		log.info("call user Join!!!");
		String nickname = requestBody.getNickname();
		String accessToken = requestBody.getAccessToken();
		String email = requestBody.getEmail();

		return userLoginService.join(email, nickname, accessToken);
	}

}
