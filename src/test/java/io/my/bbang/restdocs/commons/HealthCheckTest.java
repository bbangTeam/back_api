package io.my.bbang.restdocs.commons;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import io.my.bbang.commons.base.RestDocAttributes;
import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class HealthCheckTest extends RestDocsBaseWithSpringBoot {

	@Test
	void healthcheck() {
		ResponseFieldsSnippet responseSnippet = 
				responseFields(
						fieldWithPath("result")
							.description("결과")
							.attributes(
									RestDocAttributes.length(0),
									RestDocAttributes.format("")),
						fieldWithPath("code")
							.description("응답 코드")
							.attributes(
									RestDocAttributes.length(0),
									RestDocAttributes.format("integer"))
				);
		
		getWebTestClientNotAuth("/api/healthcheck").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/healthcheck", responseSnippet));
	}

}
