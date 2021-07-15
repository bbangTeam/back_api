package io.my.bbang.restdocs.comment;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.comment.dto.CommentListDto;
import io.my.bbang.comment.payload.request.CommentWriteRequest;
import io.my.bbang.comment.payload.response.CommentCountResponse;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.payload.response.CommentWriteResponse;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import reactor.core.publisher.Mono;

class CommentTest extends RestDocsBaseWithSpringBoot {

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("REST Docs 댓글 목록")
	void list() throws JsonProcessingException {

		int pageSize = 10;
		int pageNum = 0;

		CommentListResponse responseBody = new CommentListResponse();
		
		responseBody.setResult("Success");
		
		for (int index=0; index<pageSize; index++) {
			CommentListDto dto = new CommentListDto();
			dto.setNickname("빵터짐" + index);
			dto.setContent("맛있어보여요." + index);
			
			responseBody.getCommentList().add(dto);
		}

		Mockito.when(commentService.list(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mono.just(responseBody));


		StringBuilder params = new StringBuilder();
		params.append("?")
				.append("id")
				.append("=")
				.append("seoul001")
				.append("&")
				.append("&")
				.append("pageNum")
				.append("=")
				.append(pageNum)
				.append("&")
				.append("pageSize")
				.append("=")
				.append(pageSize)
		;
		
		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("id").description("게시글(혹은 상위 댓글) 고유 번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						parameterWithName("pageSize").description("페이지당 댓글 개수")
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
						fieldWithPath("code").description("응답 코드")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("integer")),
						fieldWithPath("commentList.[].nickname").description("댓글 작성자")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("commentList.[].content").description("댓글 내용")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		getWebTestClient("/api/comment/list" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 댓글 작성")
	void write() throws JsonProcessingException {
		CommentWriteRequest requestBody = new CommentWriteRequest();
		requestBody.setId("seoul001");
		requestBody.setType("breadstagram");
		requestBody.setContent("맛있어보여요");

		CommentWriteResponse responseBody = new CommentWriteResponse();
		responseBody.setResult("Success");
		responseBody.setId(UUID.randomUUID().toString());

		Mockito.when(commentService.write(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Mono.just(responseBody));
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("id").description("게시글(혹은 상위 댓글) 고유 번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						fieldWithPath("type").description("게시글 종류 (ex.빵스타그램)")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")),
						fieldWithPath("content").description("댓글 내용")
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
						fieldWithPath("id").description("댓글 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"))
				);
		
		postWebTestClient(requestBody, "/api/comment/write").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/write", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 댓글 갯수")
	void count() throws JsonProcessingException {

		CommentCountResponse responseBody = new CommentCountResponse();
		responseBody.setCount(5);
		responseBody.setResult("Success");

		Mockito.when(commentService.count(Mockito.any())).thenReturn(Mono.just(responseBody));

		StringBuilder params = new StringBuilder();
		params.append("?")
				.append("id")
				.append("=")
				.append("comment-id-01")
		;
		
		RequestParametersSnippet requestSnippet = 
				requestParameters(
						parameterWithName("id").description("게시글(혹은 상위 댓글) 고유 번호")
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
													RestDocAttributes.format("integer")),
						fieldWithPath("count").description("댓글 갯수")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("Integer"))
				);
		
		getWebTestClient("/api/comment/count" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/count", requestSnippet, responseSnippet));
	}

}
