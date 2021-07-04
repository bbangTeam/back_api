package io.my.bbang.restdocs.area;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class AreaTest extends RestDocsBaseWithSpringBoot {

	@Test
	@DisplayName("REST Docs 지역 목록 API")
	void list() {
		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("areaList.[].id").description("지역 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("areaList.[].name").description("지역명")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		getWebTestClient("/api/area/list").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", responseSnippet));
	}
    
}
