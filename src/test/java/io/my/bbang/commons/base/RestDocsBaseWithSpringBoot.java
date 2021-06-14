package io.my.bbang.commons.base;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.my.bbang.user.repository.UserRepository;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RestDocsBaseWithSpringBoot extends TestBase {
	private String authorization = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjIyOTA0MDA1LCJ1c2VySWQiOiI2MGI3NzkzNTU4NjdlODExZTdmYWRiZTAifQ.Sb91WW9CaEwc-H5jQYGQAPGvnVQG3UJss6Fi5WGP1Apq6nIpvZDdRxuQs8sQTfp-z1oLFEHPePvnwKOXxHDg2g";

	protected WebTestClient webTestClient;
	protected Snippet defaultRequestHeader;
	
    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    protected UserRepository testRepository;
    
	@BeforeEach
	void setUp(ApplicationContext applicationContext,
			RestDocumentationContextProvider restDocumentation) {
		this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
				.configureClient()
				.baseUrl("http://125.240.27.115:7000") 
				.filter(documentationConfiguration(restDocumentation)) 
				.filter(documentationConfiguration(restDocumentation).snippets().withEncoding("UTF-8"))
				.build();

    	this.defaultRequestHeader = requestHeaders(
				headerWithName("Authorization")
					.description("JWT 인증 토큰")
					.optional()
	    			.attributes(
	    					RestDocAttributes.length(""),
	    					RestDocAttributes.format("")
					)
    			);
	}
	
	protected ResponseSpec getWebTestClient(String uri) {
		return this.webTestClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).exchange();
	}
	
	protected ResponseSpec getWebTestClient(Object body, String uri) {
		return this.webTestClient.method(HttpMethod.GET).uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected ResponseSpec postWebTestClient(Object body, String uri) {
		return this.webTestClient.post().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected ResponseSpec putWebTestClient(Object body, String uri) {
		return this.webTestClient.put().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected ResponseSpec getWebTestClientNotAuth(String uri) {
		return this.webTestClient.get().uri(uri).accept(MediaType.APPLICATION_JSON).exchange();
	}
	
	protected ResponseSpec getWebTestClientNotAuth(Object body, String uri) {
		return this.webTestClient.method(HttpMethod.GET).uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected ResponseSpec postWebTestClientNotAuth(Object body, String uri) {
		return this.webTestClient.post().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected ResponseSpec putWebTestClientNotAuth(Object body, String uri) {
		return this.webTestClient.put().uri(uri).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
			String fileName, 
			RequestFieldsSnippet requestFieldsSnippet, 
			ResponseFieldsSnippet responseFieldsSnippet) {
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				requestFieldsSnippet,
				responseFieldsSnippet);
	}
	
	protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
			String fileName, 
			RequestFieldsSnippet requestFieldsSnippet) {
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				requestFieldsSnippet);
	}
	
	protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
			String fileName, 
			ResponseFieldsSnippet responseFieldsSnippet) {
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				responseFieldsSnippet);
	}
	
	protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
			String fileName, 
			RequestParametersSnippet requestParametersSnippet, 
			ResponseFieldsSnippet responseFieldsSnippet) {
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				requestParametersSnippet,
				responseFieldsSnippet);
	}
	
	protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
			String fileName, 
			RequestParametersSnippet requestParametersSnippet) {
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				requestParametersSnippet);
	}
	
}
