package io.my.bbang.restdocs.pilgrimage;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
	void list() throws JsonProcessingException {
		String option = "all";

		PilgrimageListResponse responseBody = new PilgrimageListResponse();

		responseBody.setResult("Success");
		
		for (int i=0; i<15; i++) {
			PilgrimageListDto dto = new PilgrimageListDto();
			dto.setStoreName("bakery" + i);
			dto.setId(UUID.randomUUID().toString());
			dto.setClear(i%2==0);
			dto.setLatitude(37.555107 + (i / 1000d));
			dto.setLongitude(126.970691 + (i / 1000d));
			
			if (option.equals("all")) {
				List<Integer> bakeTimeList = new ArrayList<>();
				
				dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
				dto.setOpeningHours("07 ~ 20");
				dto.setBreadName("소보루");
				dto.setBakeTimeList(bakeTimeList);
				
				for (int j=8; j<20; j+=2) {
					bakeTimeList.add(j);
				}
			}
			
			responseBody.getStoreList().add(dto);
		}

		Mockito.when(pilgrimageService.list(Mockito.any(), Mockito.any())).thenReturn(Mono.just(responseBody));

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
						fieldWithPath("storeList.[].id").description("빵집 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
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
    
	
}
