package io.my.bbang.restdocs.commons;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class HealthCheck extends RestDocsBaseWithSpringBoot {

	@Test
	@Disabled
	@DisplayName("healthcheck")
	void healthCheck() throws Exception {

		this.webTestClient.get().uri("/").exchange()
			.expectStatus().isOk().expectBody()
			.consumeWith(document(
					"healthcheck", 
					responseFields(fieldWithPath("message").description("healthcheck")
							.attributes(RestDocAttributes.length(""),
										RestDocAttributes.format(""),
										RestDocAttributes.etc("")))));
		
//		ResponseSpec responseSpec = webTestClientGet("/api/healthcheck", "");

//		responseSpec.expectStatus().isOk()
//					.expectBody()
//					.consumeWith(
//							WebTestClientRestDocumentation.document(
//									"{class-name}/{method-name}",
//									preprocessRequest(prettyPrint()), 
//									preprocessResponse(prettyPrint()), 
//									defaultRequestHeader,
//									responseFields(fieldWithPath("message").description("healthcheck")
//								.attributes(RestDocAttributes.length("Integer.MAX_VALUE")))));
	}
}
