package io.my.bbang.restdocs.breadstore;

import io.my.bbang.breadstore.payload.resposne.StoreListResponse;
import io.my.bbang.commons.base.RestDocAttributes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class BreadstoreTest extends RestDocsBaseWithSpringBoot {
    
    @Test
    @DisplayName("REST Docs 빵집 목록")
    void list() {
        double longitude = 127.053609;
        double latitude = 37.499238;
        int maxDistance = 4000;
        int minDistance = 0;

        StoreListResponse responseBody = new StoreListResponse();
        List<StoreListResponse.StoreList> list = new ArrayList<>();

        for (int index = 0; index < 3; index++) {
            StoreListResponse.StoreList dto = new StoreListResponse.StoreList();
            dto.setId(UUID.randomUUID().toString());
            dto.setLongitude(longitude);
            dto.setLatitude(latitude);
            dto.setStoreName(UUID.randomUUID().toString());
            dto.setImageUrl(UUID.randomUUID().toString());
            dto.setOpenHour("매일 00:00 ~ 00:00시, 설명");
            dto.setTel("02-568-062" + index);
            dto.setLoadAddr("서울 강남구 도곡로 405 삼환 아르누보2 1층 10" + index + "호");
            dto.setStar(index + 1.45);
            dto.setReviewCount(index * 100L);
            list.add(dto);
        }
        responseBody.setStoreList(list);

        Mockito.when(storeService.list(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestSnippet =
                requestParameters(
                        parameterWithName("longitude").description("경도")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Double")),
                        parameterWithName("latitude").description("위도")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Double")),
                        parameterWithName("maxDistance").description("최대 거리 (default: 1000)").optional()
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        parameterWithName("minDistance").description("최소 거리 (default: 0)").optional()
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
                        fieldWithPath("storeList.[].id").description("가게 고유 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeList.[].storeName").description("가게명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeList.[].longitude").description("위도")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Double")),
                        fieldWithPath("storeList.[].latitude").description("경도")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Double")),
                        fieldWithPath("storeList.[].imageUrl").description("사진 경로")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeList.[].openHour").description("운영 시간")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeList.[].tel").description("연락처")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeList.[].loadAddr").description("주소")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeList.[].star").description("별점 ( 0 ~ 5 )")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Double")),
                        fieldWithPath("storeList.[].reviewCount").description("빵스타그램 게시글 수")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer"))
                );

        String params = "?" +
                "longitude" +
                "=" +
                longitude +
                "&" +
                "latitude" +
                "=" +
                latitude +
                "&" +
                "maxDistance" +
                "=" +
                maxDistance +
                "&" +
                "minDistance" +
                "=" +
                minDistance;
        getWebTestClient("/api/store/list" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
    }

}
