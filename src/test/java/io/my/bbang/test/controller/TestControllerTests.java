package io.my.bbang.test.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.my.commons.base.SpringBootTestBase;

class TestControllerTests extends SpringBootTestBase {

	@Test
	@DisplayName("login success test")
	void login() {
		String loginId = "loginId";
		
//		Mono<TestResponse> testResponse = testService.addTodo(title);
//		
//		testResponse.map(response -> response.getTitle()).subscribe(arg -> assertEquals(arg, title));
//		
//		Flux<TestDocument> testDocument = testRepository.findAll();
//		
//		testDocument.map(document -> document.getTitle()).subscribe(arg -> assertEquals(arg, title));
	}

}
