package io.my.bbang.restdocs.breadstagram;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import reactor.core.publisher.Mono;

class BreadstagramTest extends RestDocsBaseWithSpringBoot {

	@Test
	@DisplayName("REST Docs 빵스타그램 목록")
	void list() {
		int pageSize = 10;

		BreadstagramListResponse responseBody = new BreadstagramListResponse();
		responseBody.setResult("Success");

		for (int i=0; i<pageSize; i++) {
			BreadstagramListDto dto = new BreadstagramListDto();
			
			List<BreadstagramImageDto> imageList = new ArrayList<>();
			
			for (int index=0; index< pageSize - 5; index++) {
				BreadstagramImageDto image = new BreadstagramImageDto();
				image.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
				image.setNum(index);
				imageList.add(image);
			}
			List<String> breadNameList = new ArrayList<>();
			breadNameList.add("소보루");
			breadNameList.add("바게트");

			dto.setBreadNameList(breadNameList);
			dto.setBreadStoreName("breadStore" + i);
			dto.setContent("content" + UUID.randomUUID());
			dto.setCityName("Seoul");
			dto.setImageList(imageList);
			dto.setCreateDate(LocalDateTime.now());
			dto.setModifyDate(LocalDateTime.now());
			dto.setNickname("nickname" + i);
			dto.setStar(3.0);

			dto.setLike(false);
			dto.setLikeCount((int)((Math.random()*100000)));
			dto.setCommentCount((int)((Math.random()*100000)));
			dto.setClickCount((int)((Math.random()*100000)));
			
			dto.setId("bread-stagram-id" + i);
			responseBody.getBreadstagramList().add(dto);
		}

		Mockito.when(breadstagramService.list(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mono.just(responseBody));

		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("pageSize").description("페이지당 게시글 개수 (default: 5)").optional()
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer")),
						parameterWithName("pageNum").description("페이지 번호").optional()
											.attributes(
													RestDocAttributes.length(0),
													RestDocAttributes.format("Integer"))
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
						fieldWithPath("breadstagramList.[].id").description("게시글 고유 번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].cityName").description("도시명")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].breadStoreName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("breadstagramList.[].breadNameList.[]").description("먹은 빵 목록")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].content").description("본문")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].like").description("좋아요 여부")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("boolean")),
						fieldWithPath("breadstagramList.[].likeCount").description("좋아요 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Integer")),
						fieldWithPath("breadstagramList.[].commentCount").description("댓글 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Integer")),
						fieldWithPath("breadstagramList.[].clickCount").description("조회수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Integer")),
						fieldWithPath("breadstagramList.[].star").description("가게 평점")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Double")),
						fieldWithPath("breadstagramList.[].nickname").description("작성자 닉네임")
										.attributes(
												RestDocAttributes.length(0),
												RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].createDate").description("작성일")
										.attributes(
												RestDocAttributes.length(0),
												RestDocAttributes.format("DateTime")),
						fieldWithPath("breadstagramList.[].modifyDate").description("수정일")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("DateTime")),
						fieldWithPath("breadstagramList.[].imageList.[].imageUrl").description("사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].imageList.[].num").description("사진 순서")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);

		String params = "?" +
				"pageNum" +
				"=" +
				"1" +
				"&" +
				"pageSize" +
				"=" +
				"3";
		getWebTestClient("/api/breadstagram/list" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}
	

	@Test
	@DisplayName("REST Docs 빵스타그램 글쓰기")
	void write() {
		BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();
		
		responseBody.setId(UUID.randomUUID().toString());
		responseBody.setResult("Success");
		
		Mockito.when(breadstagramService.write(Mockito.any())).thenReturn(Mono.just(responseBody));

		BreadstagramWriteRequest requestBody = new BreadstagramWriteRequest();
		List<String> breadList = new ArrayList<>();
		breadList.add("소보루");
		breadList.add("바게트");

		requestBody.setId("id");
		requestBody.setCityName("서울");
		requestBody.setStoreName("빵터짐");
		requestBody.setBreadNameList(breadList);
		requestBody.setContent("피자빵!!!");
		
		List<BreadstagramImageDto> imageList = new ArrayList<>();

		for (int index=0; index<(int)((Math.random()*10000)%10)+1; index++) {
			BreadstagramImageDto dto = new BreadstagramImageDto();
			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			dto.setNum(index);
			imageList.add(dto);
		}
		
		requestBody.setImageList(imageList);
		
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("id").description("빵집 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("cityName").description("도시명")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("storeName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("breadNameList.[]").description("먹은 빵 목록")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("content").description("내용")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("imageList.[].imageUrl").description("사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("imageList.[].num").description("사진 순서")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"))
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
						fieldWithPath("id").description("게시글 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		postWebTestClient(requestBody, "/api/breadstagram").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/write", requestSnippet, responseSnippet));
	}

}
