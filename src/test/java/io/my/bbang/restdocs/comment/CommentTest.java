package io.my.bbang.restdocs.comment;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.my.bbang.comment.payload.request.CommentListRequest;
import io.my.bbang.comment.payload.request.CommentWriteRequest;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class CommentTest extends RestDocsBaseWithSpringBoot {

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("REST Docs 댓글 목록")
	void list() throws JsonProcessingException {
		CommentListRequest requestBody = new CommentListRequest();
		requestBody.setId("seoul001");
		requestBody.setType("breadstagram");
		requestBody.setPageNum(1);
		requestBody.setPageSize(3);
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("id").description("도시 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("type").description("게시글 종류 (ex.빵스타그램)")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")),
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
						fieldWithPath("commentList.[].nickname").description("댓글 작성자")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")),
						fieldWithPath("commentList.[].content").description("댓글 내용")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc(""))
				);
		
		getWebTestClient(requestBody, "/api/comment/list").expectStatus()
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
		
		RequestFieldsSnippet requestSnippet = 
				requestFields(
						fieldWithPath("id").description("게시글 번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc("")), 
						fieldWithPath("type").description("게시글 종류 (ex.빵스타그램)")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"),
													RestDocAttributes.etc("")),
						fieldWithPath("content").description("댓글 내용")
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
						fieldWithPath("id").description("댓글 고유번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String"), 
													RestDocAttributes.etc(""))
				);
		
		putWebTestClient(requestBody, "/api/comment/write").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/write", requestSnippet, responseSnippet));
	}

}
