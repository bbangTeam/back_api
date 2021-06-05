package io.my.bbang.restdocs.breadstagram;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramListRequest;
import io.my.bbang.breadstagram.payload.request.BreadstagramViewRequest;
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
		BreadstagramListRequest requestBody = new BreadstagramListRequest();
		requestBody.setPageNum(1);
		requestBody.setPageSize(3);
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("pageSize").description("페이지당 댓글 개수")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"),
													RestDocAttributes.etc("")),
						fieldWithPath("pageNum").description("페이지 번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"),
													RestDocAttributes.etc(""))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("breadstagramList.[].cityName").description("도시명")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("breadstagramList.[].breadStoreName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")), 
						fieldWithPath("breadstagramList.[].breadName").description("먹은 빵")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("breadstagramList.[].like").description("좋아요")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"), 
													RestDocAttributes.etc("")),
						fieldWithPath("breadstagramList.[].tagList.[]").description("태그")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("breadstagramList.[].imageUrlList.[]").description("사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc(""))
				);
		
		getWebTestClient(requestBody, "/api/breadstagram/list").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}
	
	@Test
	@DisplayName("REST Docs 빵스타그램 상세화면")
	void view() throws JsonProcessingException {
		BreadstagramViewRequest requestBody = new BreadstagramViewRequest();
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("id").description("게시글 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc(""))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("cityName").description("도시명")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("storeName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")), 
						fieldWithPath("breadName").description("먹은 빵")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("nickname").description("작성자")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("like").description("좋아요")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"), 
													RestDocAttributes.etc("")),
						fieldWithPath("tagList.[]").description("태그")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("imageList.[].id").description("사진 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("imageList.[].num").description("사진 순서")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("imageList.[].imageUrl").description("사진 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc(""))
				);
		
		getWebTestClient(requestBody, "/api/breadstagram/view").expectStatus()
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
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")), 
						fieldWithPath("cityName").description("도시명")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("storeName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")), 
						fieldWithPath("breadName").description("먹은 빵")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("content").description("내용")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("tagList.[]").description("태그")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("imageList.[].id").description("사진 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("imageList.[].num").description("사진 순서")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"), 
													RestDocAttributes.etc(""))
				);
		
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("id").description("게시글 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc(""))
				);
		
		putWebTestClient(requestBody, "/api/breadstagram/write").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/write", requestSnippet, responseSnippet));
	}
}
