package io.my.bbang.restdocs.test;

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
import io.my.bbang.test.entity.TestEntity;
import io.my.bbang.test.vo.request.TestJoinRequest;
import io.my.bbang.test.vo.request.TestLoginRequest;

class TestTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
		testRepository.deleteAll().subscribe();
	}

	@Test
	@DisplayName("REST Docs JOIN 테스트 API 명세서")
	void join() throws JsonProcessingException {
		
		TestJoinRequest requestBody = new TestJoinRequest();
		requestBody.setName("빵터짐");
		requestBody.setLoginId("BbangTeam");
		requestBody.setPassword("BbangPassword");
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("name").description("이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("password").description("비밀번호")
											.attributes(
													RestDocAttributes.length(8, 20), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc(""))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("id").description("회원 고유 ID")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("createTime").description("생성 시간")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("DateTime"),
													RestDocAttributes.etc(""))
				);
		
		putWebTestClient(requestBody, "/api/test/join").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/join", requestSnippet, responseSnippet));
	}
	
	@Test
	@DisplayName("REST Docs LOGIN 테스트 API 명세서")
	void login() throws JsonProcessingException {
		
		String loginId = "BbangTeam";
		String password = "BbangPassword";
		
		TestEntity entity = TestEntity.newInstance(loginId, passwordEncoder.encode(password));
		entity.setName("빵터짐");
		
		testRepository.save(entity).subscribe();
		
		TestLoginRequest requestBody = new TestLoginRequest();
		requestBody.setLoginId(loginId);
		requestBody.setPassword(password);
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("password").description("비밀번호")
											.attributes(
													RestDocAttributes.length(8, 20), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc(""))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("loginId").description("로그인 ID")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("accessToken").description("Json Web AccessToken")
											.attributes(
													RestDocAttributes.length(6, 20), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("유효기간: 1시간")),
						fieldWithPath("refreshToken").description("Json Web RefreshToken")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("유효기간: 30일"))
				);
		
		postWebTestClient(requestBody, "/api/test/login").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/login", requestSnippet, responseSnippet));
	}
	
}
