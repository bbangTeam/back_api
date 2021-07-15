package io.my.bbang.restdocs.user;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.request.UserLoginRequest;

class UserLoginTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
		userRepository.deleteAll().subscribe();
	}

	@Test
	@DisplayName("REST Docs JOIN API 명세서")
	void join() throws JsonProcessingException {
		
		UserJoinRequest requestBody = new UserJoinRequest();
		requestBody.setName("빵터짐");
		requestBody.setLoginId("BbangTeam");
		requestBody.setPassword("BbangPassword");
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("name").description("이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String")),
						fieldWithPath("password").description("비밀번호")
											.attributes(
													RestDocAttributes.length(8, 20), 
													RestDocAttributes.format("String"))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("id").description("회원 고유 ID")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String")),
						fieldWithPath("createTime").description("생성 시간")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("DateTime")),
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("code").description("응답 코드")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("integer"))
				);
		
		putWebTestClientNotAuth(requestBody, "/join").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/join", requestSnippet, responseSnippet));
	}
	
	@Test
	@DisplayName("REST Docs LOGIN API 명세서")
	void login() throws JsonProcessingException {
		
		String loginId = "BbangTeam";
		String password = "BbangPassword";
		
		User user = User.newInstance(loginId, passwordEncoder.encode(password));
		user.setName("빵터짐");
		
		userRepository.save(user).subscribe();
		
		UserLoginRequest requestBody = new UserLoginRequest();
		requestBody.setLoginId(loginId);
		requestBody.setPassword(password);
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String")),
						fieldWithPath("password").description("비밀번호")
											.attributes(
													RestDocAttributes.length(8, 20), 
													RestDocAttributes.format("String"))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String")), 
						fieldWithPath("accessToken").description("Json Web AccessToken - 유효기간: 720시간")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String")),
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("code").description("응답 코드")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("integer"))
				);
		
		postWebTestClientNotAuth(requestBody, "/login").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/login", requestSnippet, responseSnippet));
	}
	

}
