package io.my.bbang.restdocs.breadstore;

import io.my.bbang.breadstore.payload.request.StoreMenuPatchRequest;
import io.my.bbang.breadstore.payload.request.StoreMenuPostRequest;
import io.my.bbang.breadstore.payload.resposne.StoreBreadListResponse;
import io.my.bbang.breadstore.payload.resposne.StoreListResponse;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.payloads.BbangResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
            dto.setLikeCount(index * 3);
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
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("storeList.[].likeCount").description("가게 좋아요 수")
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

    @Test
    @DisplayName("REST Docs 빵집 메뉴 목록")
    void stroeBreadList() {
        StoreBreadListResponse responseBody = new StoreBreadListResponse();
        List<StoreBreadListResponse.BreadList> list = new ArrayList<>();

        for (int index=0; index<3; index++) {
            StoreBreadListResponse.BreadList bread = new StoreBreadListResponse.BreadList();
            bread.setModifyDate(LocalDateTime.now());
            bread.setName("bread");
            bread.setId(UUID.randomUUID().toString());
            bread.setPrice(1000);
            list.add(bread);
        }
        responseBody.setBreadList(list);

        String id = "storeId0184201adfawe";
        Mockito.when(storeService.stroeBreadList(id)).thenReturn(Mono.just(responseBody));

        RequestParametersSnippet requestSnippet =
                requestParameters(
                        parameterWithName("id").description("빵집 고유 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Double"))
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
                        fieldWithPath("breadList.[].id").description("메뉴 고유 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("breadList.[].name").description("메뉴명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("breadList.[].price").description("가격")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("integer")),
                        fieldWithPath("breadList.[].modifyDate").description("마지막 수정일")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("date"))
                );

        String params = "?" +
                "id" +
                "=" +
                id;
        getWebTestClient("/api/store/bread/list" + params).expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/bread/list", requestSnippet, responseSnippet));
    }

    @Test
    @DisplayName("REST Docs 빵집 메뉴 등록")
    void postStoreBread() {
        StoreMenuPostRequest requestBody = new StoreMenuPostRequest();
        requestBody.setName("breadName");
        requestBody.setPrice(3000);
        List<String> list = new ArrayList<>();
        list.add("2021-09-01 11:10");
        requestBody.setBakeTimeList(list);
        requestBody.setStoreId("storeId001adfawe");

        Mockito.when(storeService.postStoreBread(Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

        RequestFieldsSnippet requestSnippet =
                requestFields(
                        fieldWithPath("storeId").description("빵집 id")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("name").description("메뉴명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("price").description("가격")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("bakeTimeList.[]").description("빵 나오는 시간 (yyyy-MM-dd HH:mm)")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("[]"))
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

        postWebTestClient(requestBody, "/api/store/bread").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/bread/post", requestSnippet, responseSnippet));
    }

    @Test
    @DisplayName("REST Docs 빵집 메뉴 수정")
    void patchStoreBread() {
        StoreMenuPatchRequest requestBody = new StoreMenuPatchRequest();
        requestBody.setId("storeMenuId01941asdfwa");
        requestBody.setName("breadName");
        requestBody.setPrice(3000);
        List<String> list = new ArrayList<>();
        list.add("2021-09-01 11:10");
        requestBody.setBakeTimeList(list);
        requestBody.setStoreId("storeId001adfawe");

        Mockito.when(storeService.patchStoreBread(Mockito.any())).thenReturn(Mono.just(new BbangResponse()));

        RequestFieldsSnippet requestSnippet =
                requestFields(
                        fieldWithPath("id").description("빵집 메뉴 고유 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("storeId").description("빵집 고유 번호")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("name").description("메뉴명")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("String")),
                        fieldWithPath("price").description("가격")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("Integer")),
                        fieldWithPath("bakeTimeList.[]").description("빵 나오는 시간")
                                .attributes(
                                        RestDocAttributes.length(0),
                                        RestDocAttributes.format("yyyy-MM-dd HH:mm"))
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

        patchWebTestClient(requestBody, "/api/store/bread").expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(createConsumer("/bread/patch", requestSnippet, responseSnippet));
    }


}
