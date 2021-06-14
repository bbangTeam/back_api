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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class BreadstagramTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("REST Docs 빵스타그램 목록")
	void list() throws JsonProcessingException {
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
						fieldWithPath("breadstagramList.[].tagList.[]").description("태그")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("breadstagramList.[].imageUrlList.[]").description("사진 경로")
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
	@DisplayName("REST Docs 빵스타그램 상세화면")
	void view() throws JsonProcessingException {
		StringBuilder params = new StringBuilder();
		params.append("?")
				.append("id")
				.append("=")
				.append("id001")
		;
		
		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("id").description("게시글 고유번호")
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
						fieldWithPath("nickname").description("작성자")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("like").description("좋아요")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer")),
						fieldWithPath("tagList.[]").description("태그")
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
													RestDocAttributes.format("Integer")), 
						fieldWithPath("imageList.[].imageUrl").description("사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		getWebTestClient("/api/breadstagram/view" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/view", requestSnippet, responseSnippet));
	}
	

	@Test
	@DisplayName("REST Docs 빵스타그램 글쓰기")
	void write() throws JsonProcessingException {
		BreadstagramWriteRequest requestBody = new BreadstagramWriteRequest();
		requestBody.setId("id");
		requestBody.setCityName("서울");
		requestBody.setStoreName("빵터짐");
		requestBody.setBreadName("피자빵");
		requestBody.setContent("피자빵!!!");
		

		List<String> tagList = new ArrayList<>();
		List<BreadstagramImageDto> imageList = new ArrayList<>();

		tagList.add("피지빵");
		tagList.add("서울빵집");

		for (int index=0; index<(int)((Math.random()*10000)%10)+1; index++) {
			BreadstagramImageDto dto = new BreadstagramImageDto();
			dto.setId(UUID.randomUUID().toString());
			dto.setNum(index);
			imageList.add(dto);
		}
		
		requestBody.setTagList(tagList);
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
						fieldWithPath("tagList.[]").description("태그")
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
}
