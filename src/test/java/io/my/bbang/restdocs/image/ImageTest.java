package io.my.bbang.restdocs.image;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.BodyInserters.MultipartInserter;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.image.payload.UploadResponse;
import reactor.core.publisher.Mono;

class ImageTest extends RestDocsBaseWithSpringBoot {
	
	@BeforeEach
	void setUp() {
		
	}
	
	@Test
	@DisplayName("이미지 업로드")
	void upload() {
		String fileName = UUID.randomUUID() + "image.jpg";
		
		UploadResponse responseBody = new UploadResponse();
		responseBody.setFileName(fileName);
		responseBody.setImageUrl("http://localhost:8080/" + UUID.randomUUID() + fileName);
		responseBody.setResult("Success");
		
		Mockito.when(imageService.fileUpload(Mockito.any())).thenReturn(Mono.just(responseBody));
		
		MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
		
		Resource imageResource = new ByteArrayResource("<<jpg data>>".getBytes()) {

			@Override
			public String getFilename() {
				return "image.jpg";
			}

		};
		
		multipartData.add("file", imageResource);
		
		MultipartInserter multipartInserter = BodyInserters.fromMultipartData(multipartData);
		
		// RequestPartFieldsSnippet requestSnippet = 
		// 		requestPartFields(
		// 				fieldWithPath("file").description("파일")
		// 						.attributes(
		// 								RestDocAttributes.length(0), 
		// 								RestDocAttributes.format("multipart/form-data"))
		// 		);

		
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("fileName").description("업로드 된 파일 ID")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("imageUrl").description("이미지 url")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("result").description("결과")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		postWebTestClient(multipartInserter, "/api/image/upload").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/upload", responseSnippet));
	}
	
	@Test
	@DisplayName("이미지 삭제")
	void delete() {
		String fileName = UUID.randomUUID() + "image.jpg";
		
		String message = fileName + " delete success!";
		BbangResponse responseBody = new BbangResponse(message);
		
		Mockito.when(imageService.fileDelete(Mockito.any()))
				.thenReturn(Mono.just(responseBody));
		
		StringBuilder params = new StringBuilder();
		
		params.append("?")
				.append("fileName")
				.append("=")
				.append(fileName)
		;

		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("fileName").description("삭제할 파일 ID")
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
		
		deleteWebTestClient("/api/image/delete" + params).expectStatus()
							.isOk()
							.expectBody()
							.consumeWith(createConsumer("/delete", requestSnippet, responseSnippet));
	}

}
