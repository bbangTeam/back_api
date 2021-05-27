package io.my.bbang.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.my.commons.base.SpringBootTestBase;

class TestServiceTests extends SpringBootTestBase {

	@Test
	@DisplayName("TestService의 로그인 성공 테스트")
	void login() {
		String loginId = "loginId";
		String password = "password";
		
//		Mono<TestResponse> testResponse = testService.addTodo(title);
//		
//		testResponse.map(response -> response.getTitle()).subscribe(arg -> assertEquals(arg, title));
//		
//		Flux<TestDocument> testDocument = testRepository.findAll();
//		
//		testDocument.map(document -> document.getTitle()).subscribe(arg -> assertEquals(arg, title));
	}
	

}
