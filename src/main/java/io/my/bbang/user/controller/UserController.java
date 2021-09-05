package io.my.bbang.user.controller;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.user.dto.UserClickType;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.dto.UserStarType;
import io.my.bbang.user.payload.response.MyPageResponse;
import io.my.bbang.user.payload.response.MyRecentlyStoreResponse;
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
        return userService.modifyNickname(nickname);
    }

    @PostMapping("/click")
    public Mono<BbangResponse> userClick(
            @RequestParam("type") String type,
            @RequestParam("id") String id) {

        if (UserClickType.isExistType(type))
            return userService.click(id, type);

        throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
    }

    @PostMapping("/like")
    public Mono<BbangResponse> postUserLike(
            @RequestParam("type") String type,
            @RequestParam("id") String id) {
        log.info("POST /api/breadstagram/userLike");

        if (UserHeartType.isExistType(type))
            return userService.postLike(id, type);
        throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
    }

    @DeleteMapping("/like")
    public Mono<BbangResponse> deleteUserLike(
            @RequestParam("type") String type,
            @RequestParam("id") String id) {
        if (UserHeartType.isExistType(type))
            return userService.deleteLike(id, type);

        throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
    }

    @PostMapping("/star")
    public Mono<BbangResponse> postUserStar(
            @RequestParam("id") String id,
            @RequestParam("type") String type,
            @RequestParam("star") int star) {

        if (UserStarType.isExistType(type))
            return userService.postStar(id, type, star);

        throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
    }

    @DeleteMapping("/star")
    public Mono<BbangResponse> deleteUserStar(
            @RequestParam("id") String id,
            @RequestParam("type") String type) {

        if (UserStarType.isExistType(type))
            return userService.deleteStar(id, type);

        throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
    }

    @GetMapping("/my/page")
    public Mono<MyPageResponse> getMyPage() {
        return userService.getMyPage();
    }

    @GetMapping("/my/recently/store")
    public Mono<MyRecentlyStoreResponse> getMyRecentlyStoreList(
            @RequestParam("x") double x,
            @RequestParam("y") double y,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum) {
        return userService.getMyRecentlyStoreList(x, y, pageNum, pageSize);
    }

}
