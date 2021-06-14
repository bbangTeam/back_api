package io.my.bbang.restdocs.pilgrimage;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class PilgrimageTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("REST Docs 빵지순례 빵집 목록")
	void list() throws JsonProcessingException {
		StringBuilder params = new StringBuilder();
		params.append("?")
				.append("id")
				.append("=")
				.append("seoul001")
				.append("&")
				.append("option")
				.append("=")
				.append("all")
		;
		
		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("id").description("도시 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						parameterWithName("option").description("optional 포함 여부 - 기본 값: none").optional()
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
						fieldWithPath("storeList.[].id").description("빵집 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("storeList.[].storeName").description("빵집 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("storeList.[].isClear").description("방문 경험")
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
						fieldWithPath("storeList.[].bakeTimeList.[]").description("빵 나오는 시간")
											.optional()
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer")) 
				);
		
		getWebTestClient("/api/pilgrimage/list" + params.toString()).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}
	
}
