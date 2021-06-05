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
						fieldWithPath("message")
							.description("응답 메시지")
							.attributes(RestDocAttributes.length(Integer.MAX_VALUE), RestDocAttributes.format(""), RestDocAttributes.etc("")));
		
		getWebTestClientNotAuth("/api/healthcheck").expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(createConsumer("/healthcheck", responseSnippet));
	}

}
