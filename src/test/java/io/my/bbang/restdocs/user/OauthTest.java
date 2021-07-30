package io.my.bbang.restdocs.user;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

class OauthTest extends RestDocsBaseWithSpringBoot {

    @Test
    @DisplayName("REST Docs 회원가입 API 명세서")
    void socialLogin() {
        UserJoinRequest requestBody = new UserJoinRequest();
        requestBody.setAccessToken(jwtUtil.createAccessToken("12"));
        requestBody.setNickname("bbang");

        UserLoginResponse responseBody = new UserLoginResponse();
        responseBody.setLoginId("loginId@gmail.com");
        responseBody.setAccessToken(jwtUtil.createAccessToken(UUID.randomUUID().toString()));
        Mockito.when(oauthService.join(Mockito.any(), Mockito.any())).thenReturn(Mono.just(responseBody));

        RequestFieldsSnippet requestSnippet =
                requestFields(
                        fieldWithPath("nickname").description("설정할 닉네임")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("accessToken").description("로그인 실패시 반환받은 accessToken")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );


        ResponseFieldsSnippet responseSnippet =
                responseFields(
                        fieldWithPath("result").description("결과")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("code").description("응답 코드")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("integer")),
                        fieldWithPath("loginId").description("로그인 id (email)")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("accessToken").description("accessToken")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String"))
                );

        postWebTestClient(requestBody, "/api/oauth/join/google").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/join", requestSnippet, responseSnippet));
    }


}
