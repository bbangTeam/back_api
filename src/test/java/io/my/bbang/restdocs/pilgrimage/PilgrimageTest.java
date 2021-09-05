package io.my.bbang.restdocs.pilgrimage;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.pilgrimage.payload.request.PilgrimageWriteRequest;
import io.my.bbang.pilgrimage.payload.response.PilgrimageBoardListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.pilgrimage.dto.PilgrimageAreaListDto;
import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import io.my.bbang.pilgrimage.payload.response.PilgrimageAreaListResponse;
import io.my.bbang.pilgrimage.payload.response.PilgrimageListResponse;
import reactor.core.publisher.Mono;

class PilgrimageTest extends RestDocsBaseWithSpringBoot {
	
	@Test
	@DisplayName("REST Docs 빵지순례 빵집 목록")
	void list() {
		PilgrimageListResponse responseBody = new PilgrimageListResponse();

		responseBody.setResult("Success");
		
		for (int i=0; i<15; i++) {
			PilgrimageListDto dto = new PilgrimageListDto();
			dto.setId(UUID.randomUUID().toString());
			dto.setStoreName("bakery" + i);
			dto.setClear(i%2==0);
			dto.setLike(i%2!=0);
			dto.setLatitude(37.555107 + (i / 1000d));
			dto.setLongitude(126.970691 + (i / 1000d));

			dto.setLikeCount((long)((Math.random()*100000)));
			dto.setReviewCount((long)((Math.random()*100000)));
			dto.setStar(3.6874);

			List<Integer> bakeTimeList = new ArrayList<>();

			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			dto.setOpeningHours("07 ~ 20");
			dto.setBreadName("소보루");
			dto.setBakeTimeList(bakeTimeList);

			for (int j=8; j<20; j+=2) bakeTimeList.add(j);

			responseBody.getStoreList().add(dto);
		}

		Mockito.when(pilgrimageService.list(Mockito.any())).thenReturn(Mono.just(responseBody));

		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("id").description("도시 고유번호")
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("String")));

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
						fieldWithPath("storeList.[].id").description("빵집 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("storeList.[].star").description("평점")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("double")),
						fieldWithPath("storeList.[].storeName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("storeList.[].clear").description("방문 경험")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Boolean")), 
						fieldWithPath("storeList.[].latitude").description("위도 (y좌표)")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Double")), 
						fieldWithPath("storeList.[].longitude").description("경도 (x좌표)")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Double")), 
						fieldWithPath("storeList.[].imageUrl").description("대표 사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("storeList.[].openingHours").description("영업 시간")
											.optional()
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("storeList.[].breadName").description("대표 메뉴")
											.optional()
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("storeList.[].reviewCount").description("빵스타그램 게시글 갯수")
											.optional()
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("Integer")),
						fieldWithPath("storeList.[].likeCount").description("좋아요 갯수")
											.optional()
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("Integer")),
						fieldWithPath("storeList.[].like").description("좋아요 여부")
											.optional()
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("Boolean")),
						fieldWithPath("storeList.[].bakeTimeList.[]").description("빵 나오는 시간")
											.optional()
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"))
				);

		String params = "?" +
				"id" +
				"=" +
				"seoul001";
		getWebTestClient("/api/pilgrimage/list" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵지순례 지역 목록 API")
	void areaList() {

		PilgrimageAreaListResponse responseBody = new PilgrimageAreaListResponse();
		responseBody.setResult("Success");

		for (int i=0; i<7; i++) {
			PilgrimageAreaListDto dto = new PilgrimageAreaListDto();
			dto.setId(UUID.randomUUID().toString());
			dto.setName("area" + i);
			responseBody.getAreaList().add(dto);
		}

		Mockito.when(pilgrimageService.areaList()).thenReturn(Mono.just(responseBody));

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
						fieldWithPath("areaList.[].id").description("지역 고유번호")
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("String")),
						fieldWithPath("areaList.[].name").description("지역명")
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("String"))
				);

		getWebTestClient("/api/pilgrimage/area/list").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/arealist", responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵지순례 리뷰")
	void board() {
		PilgrimageWriteRequest requestBody = new PilgrimageWriteRequest();
		requestBody.setStoreId("storeId_01ff0d");
		requestBody.setContent("content content content");
		requestBody.setTitle("title title title");

		Mockito.when(pilgrimageService.write(Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

		RequestFieldsSnippet requestSnippet =
				requestFields(
						fieldWithPath("storeId").description("빵집 고유번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("title").description("제목")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("content").description("내용")
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

		postWebTestClient(requestBody, "/api/pilgrimage/board").expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/board", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵지순례 방문")
	void visit() {
		Mockito.when(pilgrimageService.visit(Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("id").description("빵지순례 고유 번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")));

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
				"id" +
				"=" +
				"pilgrimageId01";

		postWebTestClient("/api/pilgrimage/visit" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/visit", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵지순례 리뷰 목록 API")
	void boardList() {

		PilgrimageBoardListResponse responseBody = new PilgrimageBoardListResponse();
		List<PilgrimageBoardListResponse.Board> list = new ArrayList<>();
		for (int i=0; i<5; i++) {
			PilgrimageBoardListResponse.Board board = new PilgrimageBoardListResponse.Board();
			board.setNickname("nickname" + i);
			board.setModifyDate(LocalDateTime.now());
			board.setCreateDate(LocalDateTime.now());
			board.setTitle("title" + i);
			board.setCommentCount(i * 201);
			board.setStoreName("storeName" + i);
			board.setStoreId("storeId0124128");
			board.setId("boardId9241432");
			board.setClickCount(1000L);
			board.setContent("board content content content");
			list.add(board);
		}
		responseBody.setBoardList(list);

		Mockito.when(pilgrimageService.boardList(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mono.just(responseBody));

		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("id").description("도시 고유번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						parameterWithName("pageSize").description("페이지당 게시글 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						parameterWithName("pageNum").description("페이지 번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")));

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
						fieldWithPath("boardList.[].id").description("게시글 고유번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("boardList.[].title").description("게시글 제목")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("boardList.[].content").description("게시글 내용")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("boardList.[].storeId").description("가게 고유 번호")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("boardList.[].storeName").description("가게명")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("boardList.[].nickname").description("작성자")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("String")),
						fieldWithPath("boardList.[].commentCount").description("댓글 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						fieldWithPath("boardList.[].clickCount").description("조회수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("integer")),
						fieldWithPath("boardList.[].createDate").description("작성일")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("date")),
						fieldWithPath("boardList.[].modifyDate").description("수정일")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("date"))
				);
		String id = "storeId041341234";
		int pageSize = 5;
		int pageNum = 0;

		String params =
				"?id=" + id +
				"&pageSize=" + pageSize +
				"&pageNum=" + pageNum;


		getWebTestClient("/api/pilgrimage/board/list" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/boardList", requestSnippet, responseSnippet));
	}
}
