package io.my.bbang.user.controller;

import io.my.bbang.user.payload.response.MyProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/nickname")
    public Mono<BbangResponse> checkNickname(
        @RequestParam String nickname) {
        return userService.checkNickname(nickname);
    }

    @PatchMapping("/nickname")
    public Mono<BbangResponse> modifyNickname(
        @RequestParam String nickname) {
        return userService.modifyNickname(nickname);
    }

    @GetMapping("/my/profile")
    public Mono<MyProfileResponse> getMyProfile() {
        return userService.getMyProfile();
    }

    
}
