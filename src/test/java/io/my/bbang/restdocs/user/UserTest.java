package io.my.bbang.restdocs.user;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import io.my.bbang.user.dto.UserClickType;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.dto.UserStarType;
import io.my.bbang.user.payload.response.MyPageResponse;
import io.my.bbang.user.payload.response.MyRecentlyStoreResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
		Mockito.when(userService.click(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

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
		Mockito.when(userService.postLike(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

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

		Mockito.when(userService.deleteLike(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

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
		Mockito.when(userService.postStar(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(Mono.just(new BbangResponse()));
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
		Mockito.when(userService.deleteStar(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

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

	@Test
	@DisplayName("REST Docs 사용자 정보 조회")
	void getMyPage() {
		MyPageResponse responseBody = new MyPageResponse();
		responseBody.setEmail("bbang@bbang.com");
		responseBody.setNickname("nickname001");
		responseBody.setProfileImageUrl("https://oopy.lazyrockets.com/api/v2/notion/image?src=https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fd9c7808e-79b5-4ed8-b6c3-d7dc1239813b%2F827502_page_512x512.png&blockId=a9dbb28c-f2db-4db8-a54e-71ee14f42c98&width=256");
		responseBody.setPostCount(12);
		responseBody.setLikeCount(33);
		responseBody.setCommentCount(100);

		Mockito.when(userService.getMyPage()).thenReturn(Mono.just(responseBody));

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
						fieldWithPath("nickname").description("닉네임")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("email").description("이메일")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("profileImageUrl").description("이미지 경로")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("postCount").description("게시글 갯수 (총)")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						fieldWithPath("commentCount").description("댓글 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						fieldWithPath("likeCount").description("좋아요 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer"))
				);

		getWebTestClient("/api/user/my/page").expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/my/page", responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 사용자 최근 봤던 가게 목록 조회")
	void getMyRecentlyStoreList() {

		MyRecentlyStoreResponse responseBody = new MyRecentlyStoreResponse();
		List<MyRecentlyStoreResponse.RecentlyStore> list = new ArrayList<>();

		for (int index=0; index<3; index++) {
			MyRecentlyStoreResponse.RecentlyStore recentlyStore = new MyRecentlyStoreResponse.RecentlyStore();
			recentlyStore.setStoreName("storeName");
			recentlyStore.setFullNm("서울특별시 종로");
			recentlyStore.setSigKorNm("종로구");
			recentlyStore.setClickDate(LocalDateTime.now());
			recentlyStore.setImageUrl("https://oopy.lazyrockets.com/api/v2/notion/image?src=https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fd9c7808e-79b5-4ed8-b6c3-d7dc1239813b%2F827502_page_512x512.png&blockId=a9dbb28c-f2db-4db8-a54e-71ee14f42c98&width=256");
			recentlyStore.setDistance(300);
			recentlyStore.setId("storeId001242");
			list.add(recentlyStore);
		}

		responseBody.setList(list);

		Mockito.when(
				userService.getMyRecentlyStoreList(
						Mockito.anyDouble(),
						Mockito.anyDouble(),
						Mockito.anyInt(),
						Mockito.anyInt()))
				.thenReturn(Mono.just(responseBody));

		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("x").description("x 좌표 (위도)")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("double")),
						parameterWithName("y").description("y 좌표 (경도)")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("double")),
						parameterWithName("pageSize").description("페이지당 갯수 (기본값: 5)")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						parameterWithName("pageNum").description("페이지 번호 (기본값: 0)")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer"))
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
						fieldWithPath("list.[].id").description("빵집 고유 번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("list.[].storeName").description("빵집명")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("list.[].imageUrl").description("빵집 이미지")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("list.[].fullNm").description("행정구역명")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("list.[].sigKorNm").description("시군구명")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("string")),
						fieldWithPath("list.[].distance").description("거리, km단위")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("double")),
						fieldWithPath("list.[].clickDate").description("조회한 날짜")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("date"))
				);

		String params = "?" +
				"x" +
				"=" +
				126.99427815951064 +
				"&y" +
				"=" +
				37.540882507109906 +
				"&pageSize" +
				"=" +
				3 +
				"&pageNum" +
				"=" +
				0
		;
		getWebTestClient("/api/user/my/recently/store" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/my/recently/store", requestSnippet, responseSnippet));
	}
}
