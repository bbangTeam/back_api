package io.my.bbang.restdocs.ideal;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.ideal.dto.IdealContentDto;
import io.my.bbang.ideal.dto.IdealRankDto;
import io.my.bbang.ideal.payload.request.IdealSelectedRequest;
import io.my.bbang.ideal.payload.response.IdealContentResponse;
import io.my.bbang.ideal.payload.response.IdealRankResponse;
import reactor.core.publisher.Mono;

class IdealTest extends RestDocsBaseWithSpringBoot {

	@Test
	@DisplayName("REST Docs 빵드컵 진행 여부 확인")
	void done() {
		Mockito.when(idealService.done()).thenReturn(Mono.just(new BbangResponse()));
		
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
		
		getWebTestClient("/api/ideal/done").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/done", responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵드컵 게임")
	void content() {
		IdealContentResponse responseBody = new IdealContentResponse();
		
		responseBody.setResult("Success");
		
		for (int i=0; i<32; i++) {
			IdealContentDto dto = new IdealContentDto();
			dto.setId(UUID.randomUUID().toString());
			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			dto.setName("bread" + i);
			responseBody.getBreadList().add(dto);
		}

		Mockito.when(idealService.content()).thenReturn(Mono.just(responseBody));
		
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
						fieldWithPath("breadList.[].id").description("빵 카테고리 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadList.[].name").description("빵 카테고리 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadList.[].imageUrl").description("이미지 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		getWebTestClient("/api/ideal/content").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/content", responseSnippet));
	}


	@Test
	@DisplayName("REST Docs 빵드컵 순위 확인")
	void rank() {
		IdealRankResponse responseBody = new IdealRankResponse();

		responseBody.setResult("Success");

		for (int i=0; i<5; i++) {
			IdealRankDto dto = new IdealRankDto();
			dto.setName("bread" + i);
			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			dto.setSelectedCount((long) (1000 - i * i));
			responseBody.getBreadList().add(dto);
		}

		Mockito.when(idealService.rank()).thenReturn(Mono.just(responseBody));
		
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
						fieldWithPath("breadList.[].name").description("빵 카테고리 이름")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadList.[].imageUrl").description("이미지 경로")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("breadList.[].selectedCount").description("선택한 사람 수")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("int"))
				);
		
		getWebTestClient("/api/ideal/rank").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/rank", responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 빵드컵 1위 선택")
	void selected() {
		
		String id = "breadIdeal01";
		IdealSelectedRequest requestBody = new IdealSelectedRequest();
		requestBody.setId(id);

		BbangResponse responseBody = new BbangResponse("Success");

		Mockito.when(idealService.selected(id)).thenReturn(Mono.just(responseBody));
				
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("id").description("선택한 빵 고유 번호")
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
		
		postWebTestClient(requestBody, "/api/ideal/selected").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/selected", requestSnippet, responseSnippet));
	}
}
