package io.my.bbang.restdocs.breadstagram;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramViewResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.commons.payloads.BbangResponse;
import reactor.core.publisher.Mono;

class BreadstagramTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("REST Docs 빵스타그램 목록")
	void list() throws JsonProcessingException {
		int pageSize = 10;

		BreadstagramListResponse responseBody = new BreadstagramListResponse();
		responseBody.setResult("Success");

		for (int i=0; i<pageSize; i++) {
			BreadstagramListDto dto = new BreadstagramListDto();
			
			List<BreadstagramImageDto> imageList = new ArrayList<>();
			
			for (int index=0; index< pageSize - 5; index++) {
				BreadstagramImageDto image = new BreadstagramImageDto();
				
				image.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
				image.setId(UUID.randomUUID().toString());
				image.setNum(index);
				imageList.add(image);
			}
			
			dto.setBreadName("bread" + i);
			dto.setBreadStoreName("breadStore" + i);
			dto.setCityName("Seoul");
			dto.setImageList(imageList);
			dto.setLike((int)((Math.random()*100000)));
			
			dto.setId("bread-stagram-id" + i);
			responseBody.getBreadstagramList().add(dto);
		}

		Mockito.when(breadstagramService.list(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mono.just(responseBody));

		StringBuilder params = new StringBuilder();
		params.append("?")
				.append("pageNum")
				.append("=")
				.append("1")
				.append("&")
				.append("pageSize")
				.append("=")
				.append("3")
		;
		
		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("pageSize").description("페이지당 게시글 개수")
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
						fieldWithPath("breadstagramList.[].breadName").description("먹은 빵")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].like").description("좋아요")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer")),
						fieldWithPath("breadstagramList.[].imageList.[].id").description("사진 id")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadstagramList.[].imageList.[].imageUrl").description("사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),

						fieldWithPath("breadstagramList.[].imageList.[].num").description("사진 순서")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		getWebTestClient("/api/breadstagram/list" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}
	

	@Test
	@DisplayName("REST Docs 빵스타그램 글쓰기")
	void write() throws JsonProcessingException {
		BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();
		
		responseBody.setId(UUID.randomUUID().toString());
		responseBody.setResult("Success");
		
		Mockito.when(breadstagramService.write(Mockito.any())).thenReturn(Mono.just(responseBody));

		BreadstagramWriteRequest requestBody = new BreadstagramWriteRequest();
		requestBody.setId("id");
		requestBody.setCityName("서울");
		requestBody.setStoreName("빵터짐");
		requestBody.setBreadName("피자빵");
		requestBody.setContent("피자빵!!!");
		

		List<BreadstagramImageDto> imageList = new ArrayList<>();

		for (int index=0; index<(int)((Math.random()*10000)%10)+1; index++) {
			BreadstagramImageDto dto = new BreadstagramImageDto();
			dto.setId(UUID.randomUUID().toString());
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
						fieldWithPath("breadName").description("먹은 빵")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("content").description("내용")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("imageList.[].id").description("사진 고유번호")
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
						fieldWithPath("id").description("게시글 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		putWebTestClient(requestBody, "/api/breadstagram/write").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/write", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵스타그램 좋아요")
	void like() throws InterruptedException {
		BbangResponse responseBody = new BbangResponse("Success");

		Mockito.when(breadstagramService.like(Mockito.any(), Mockito.any())).thenReturn(Mono.just(responseBody));

		StringBuilder params = new StringBuilder();
		params.append("?")
				.append("like")
				.append("=")
				.append("true")
				.append("&")
				.append("id")
				.append("=")
				.append("storeId")
		;
		
		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("like").description("좋아요")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Boolean")),
						parameterWithName("id").description("store 고유 번호")
						.attributes(
								RestDocAttributes.length(0), 
								RestDocAttributes.format("String"))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		postWebTestClient("/api/breadstagram/like" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/like", requestSnippet, responseSnippet));
	}
	
}
