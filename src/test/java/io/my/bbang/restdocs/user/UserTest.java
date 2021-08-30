package io.my.bbang.restdocs.user;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import io.my.bbang.user.dto.UserClickType;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.dto.UserStarType;
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

	@Test
	@DisplayName("REST Docs 조회수 API 명세서")
	void userClick() {
		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("type").description("명세서 상단 타입 참고")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						parameterWithName("id").description("타입에 따른 상위 아이디")
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

		String type = UserClickType.STORE.getValue();
		String id = "storeId00aefasdfa002";

		String params = "?" +
				"id" +
				"=" +
				id +
				"&type" +
				"=" +
				type;
		postWebTestClient("/api/user/click" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/click", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 좋아요 API 명세서")
	void userLike() {
		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("type").description("명세서 상단 타입 참고")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						parameterWithName("id").description("타입에 따른 상위 아이디")
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

		String type = UserHeartType.STORE.getValue();
		String id = "storeId00aefasdfa002";

		String params = "?" +
				"id" +
				"=" +
				id +
				"&type" +
				"=" +
				type;
		postWebTestClient("/api/user/like" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/like", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 좋아요 삭제 API 명세서")
	void deleteUserLike() {
		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("type").description("명세서 상단 타입 참고")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						parameterWithName("id").description("좋아요 선택한 고유 번호")
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

		String type = UserHeartType.STORE.getValue();
		String id = "storeId00aefasdfa002";

		String params = "?" +
				"id" +
				"=" +
				id +
				"&type" +
				"=" +
				type;
		deleteWebTestClient("/api/user/like" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/delete-like", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 별점 추가 API 명세서")
	void postUserStar() {
		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("type").description("명세서 상단 타입 참고")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						parameterWithName("star").description("별점 1~5 사이 정수값")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						parameterWithName("id").description("좋아요 선택한 고유 번호")
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

		String type = UserStarType.STORE.getValue();
		String id = "storeId00aefasdfa002";

		String params = "?" +
				"id" +
				"=" +
				id +
				"&type" +
				"=" +
				type +
				"&star" +
				"=" +
				3
				;
		postWebTestClient("/api/user/star" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/star", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 별점 삭제 API 명세서")
	void deleteUserStar() {
		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("type").description("명세서 상단 타입 참고")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						parameterWithName("id").description("평점 줬던 게시글 혹은 가게 고유 번호")
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

		String type = UserHeartType.STORE.getValue();
		String id = "storeId00aefasdfa002";

		String params = "?" +
				"id" +
				"=" +
				id +
				"&type" +
				"=" +
				type;
		deleteWebTestClient("/api/user/star" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/delete-star", requestSnippet, responseSnippet));
	}
}
