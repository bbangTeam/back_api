package io.my.bbang.user.controller;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.user.dto.UserClickType;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.dto.UserStarType;
import io.my.bbang.user.payload.response.MyProfileResponse;
import org.springframework.web.bind.annotation.*;

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
        userService.modifyNickname(nickname);
        return Mono.just(new BbangResponse());
    }

    @GetMapping("/my/profile")
    public Mono<MyProfileResponse> getMyProfile() {
        return userService.getMyProfile();
    }

    @PostMapping("/click")
    public Mono<BbangResponse> userClick(
            @RequestParam("type") String type,
            @RequestParam("id") String id) {
        if (UserClickType.isExistType(type)) userService.click(id, type);
        else throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
        return Mono.just(new BbangResponse());
    }

    @PostMapping("/like")
    public Mono<BbangResponse> postUserLike(
            @RequestParam("type") String type,
            @RequestParam("id") String id) {
        log.info("POST /api/breadstagram/userLike");

        if (UserHeartType.isExistType(type)) userService.postLike(id, type);
        else throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
        return Mono.just(new BbangResponse());
    }

    @DeleteMapping("/like")
    public Mono<BbangResponse> deleteUserLike(
            @RequestParam("type") String type,
            @RequestParam("id") String id) {
        if (UserHeartType.isExistType(type)) userService.deleteLike(id, type);
        else throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
        return Mono.just(new BbangResponse());
    }

    @PostMapping("/star")
    public Mono<BbangResponse> postUserStar(
            @RequestParam("id") String id,
            @RequestParam("type") String type,
            @RequestParam("star") int star) {

        if (UserStarType.isExistType(type)) userService.postStar(id, type, star);
        else throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);

        return Mono.just(new BbangResponse());
    }

    @DeleteMapping("/star")
    public Mono<BbangResponse> deleteUserStar(
            @RequestParam("id") String id,
            @RequestParam("type") String type) {

        if (UserStarType.isExistType(type)) userService.deleteStar(id, type);
        else throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);

        return Mono.just(new BbangResponse());
    }

}
