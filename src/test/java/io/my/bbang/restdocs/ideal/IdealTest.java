package io.my.bbang.restdocs.ideal;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class IdealTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("REST Docs 빵드컵")
	void content() throws JsonProcessingException {
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("breadList.[].id").description("빵 카테고리 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("breadList.[].name").description("빵 카테고리 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")),
						fieldWithPath("breadList.[].imageUrl").description("이미지 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc(""))
				);
		
		getWebTestClient("/api/ideal/content").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/content", responseSnippet));
	}
}
