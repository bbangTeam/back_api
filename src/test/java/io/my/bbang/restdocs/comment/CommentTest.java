package io.my.bbang.restdocs.comment;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.time.LocalDateTime;
import java.util.UUID;

import io.my.bbang.comment.dto.CommentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.my.bbang.comment.dto.CommentListDto;
import io.my.bbang.comment.payload.request.CommentWriteRequest;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.payload.response.CommentWriteResponse;
import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;
import reactor.core.publisher.Mono;

class CommentTest extends RestDocsBaseWithSpringBoot {

	@Test
	@DisplayName("REST Docs 댓글 목록")
	void list() {

		int pageSize = 10;
		int pageNum = 0;

		CommentListResponse responseBody = new CommentListResponse();
		
		responseBody.setResult("Success");
		
		for (int index=0; index<pageSize; index++) {
			CommentListDto dto = new CommentListDto();
			dto.setContent("맛있어보여요." + index);
			dto.setNickname("빵터짐" + index);
			dto.setLikeCount(index);
			dto.setReCommentCount(index);
			dto.setClickCount(index);
			dto.setLike(false);
			dto.setModifyDate(LocalDateTime.now());
			dto.setCreateDate(LocalDateTime.now());

			responseBody.getCommentList().add(dto);
		}

		Mockito.when(commentService.list(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mono.just(responseBody));


		RequestParametersSnippet requestSnippet =
				requestParameters(
						parameterWithName("id").description("게시글(혹은 상위 댓글) 고유 번호")
											.attributes(
													RestDocAttributes.length(0), 
													RestDocAttributes.format("String")), 
						parameterWithName("pageSize").description("페이지당 댓글 개수 (default: 5)").optional()
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
													RestDocAttributes.format("String")),
						fieldWithPath("commentList.[].likeCount").description("좋아요 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Integer")),
						fieldWithPath("commentList.[].reCommentCount").description("대댓글 갯수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Integer")),
						fieldWithPath("commentList.[].clickCount").description("조회수")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Integer")),
						fieldWithPath("commentList.[].like").description("좋아요 여부")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("Boolean")),
						fieldWithPath("commentList.[].createDate").description("등록일")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("date")),
						fieldWithPath("commentList.[].modifyDate").description("수정일")
								.attributes(
										RestDocAttributes.length(0),
										RestDocAttributes.format("date"))
				);

		String params = "?" +
				"id" +
				"=" +
				"seoul001" +
				"&" +
				"&" +
				"pageNum" +
				"=" +
				pageNum +
				"&" +
				"pageSize" +
				"=" +
				pageSize;
		getWebTestClient("/api/comment/list" + params).expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/list", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 댓글 작성")
	void write() {
		CommentWriteRequest requestBody = new CommentWriteRequest();
		requestBody.setId("seoul001");
		requestBody.setType(CommentType.BREADSTAGRAM.getValue());
		requestBody.setContent("맛있어보여요");
		requestBody.setParentId("parendId");

		CommentWriteResponse responseBody = new CommentWriteResponse();
		responseBody.setResult("Success");
		responseBody.setId(UUID.randomUUID().toString());

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
													RestDocAttributes.format("Integer")),
						fieldWithPath("parentId").description("부모글의 고유 번호")
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
		
		postWebTestClient(requestBody, "/api/comment").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/write", requestSnippet, responseSnippet));
	}

	@Test
	@DisplayName("REST Docs 댓글 삭제")
	void delete() {
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
										RestDocAttributes.format("integer"))
				);

		String params = "?" +
				"id" +
				"=" +
				"commentId001dfafew"
				;
		deleteWebTestClient("/api/comment" + params).expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(createConsumer("/delete", requestSnippet, responseSnippet));
	}
}
