package io.my.bbang.restdocs.test;

import io.my.bbang.commons.base.RestDocsBaseWithSpringBoot;

class TestController extends RestDocsBaseWithSpringBoot {
//
//	@Test
//	@DisplayName("healthcheck")
//	void healthCheck() throws Exception {
//		SuccessModel request = new SuccessModel();
//
//		ResultActions result = this.mockMvc.perform(post("/api/healthcheck")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(request))
//				.headers(getTestHeader()));
//		
//		result.andDo(print()).andExpect(status().isOk())
//				.andExpect(jsonPath("result.code")
//						.value(HttpResponseStatus.SUCCESS.getCode()))
//				.andDo(this.document.document(
//						this.defaultRequestHeader,
//						responseFields(
//								fieldWithPath("result.code")
//									.description("결과 코드")
//									.attributes(RestDocAttributes.length(Integer.MAX_VALUE), RestDocAttributes.format(""), RestDocAttributes.etc("")))));
//	}
}
