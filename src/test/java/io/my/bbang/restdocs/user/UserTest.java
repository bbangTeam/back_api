package io.my.bbang.restdocs.user;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.commons.payloads.BbangResponse;
import reactor.core.publisher.Mono;

class UserTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
		userRepository.deleteAll().subscribe();
	}

	@Test
	@DisplayName("REST Docs 닉네임 중복 확인 API 명세서")
	void checkNickname() {

		Mockito.when(userService.checkNickname(Mockito.any())).thenReturn(Mono.just(new BbangResponse("Success")));
		
		RequestParametersSnippet requestSnippet = 
			requestParameters(
					parameterWithName("nickname").description("확인할 닉네임")
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
													RestDocAttributes.format("integer"))
				);

		String params = "?" +
				"nickname" +
				"=" +
				"testNickname1";
		getWebTestClient("/api/user/nickname" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/check-nickname", requestSnippet, responseSnippet));
	}
	
	@Test
	@DisplayName("REST Docs 닉네임 변경 API 명세서")
	void modifyNickname() {

		Mockito.when(userService.modifyNickname(Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

		RequestParametersSnippet requestSnippet =
		requestParameters(
				parameterWithName("nickname").description("변경할 닉네임")
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
													RestDocAttributes.format("integer"))
				);

		String params = "?" +
				"nickname" +
				"=" +
				"testNickname1";
		patchWebTestClient("/api/user/nickname" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/modify-nickname", requestSnippet, responseSnippet));
	}
	

}
