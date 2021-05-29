package io.my.bbang.commons.base;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class RestDocsBaseWithSpringBoot extends TestBase {
	
	@Autowired
	protected ApplicationContext context;
	
	protected WebTestClient webTestClient;
	
	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.webTestClient = WebTestClient.bindToApplicationContext(context)
			.configureClient()
			.filter(documentationConfiguration(restDocumentation))
			.build();
	}
	
    @Autowired
    protected ObjectMapper objectMapper;

//    protected ErrorResponse errResp =ErrorResponse.builder()
//			.result()
//			.build();
    
    protected Snippet defaultRequestHeader;
    
    @BeforeEach
    public void setup(WebApplicationContext context, RestDocumentationContextProvider restDocument) {
    	this.webTestClient= WebTestClient.bindToApplicationContext(context)
    									.configureClient()
    									.filter(documentationConfiguration(restDocument))
										.filter(documentationConfiguration(restDocument).snippets().withEncoding("UTF-8"))
										.build();
    	
    	
    	this.defaultRequestHeader = requestHeaders(
				headerWithName("Authorization")
					.description("JWT 인증 토큰")
	    			.attributes(
	    					RestDocAttributes.length(""),
	    					RestDocAttributes.format(""),
	    					RestDocAttributes.etc("JWT 인증 토큰")
					)
    			);
    }
    
    public void apiNotDevelopedYet(String uri) {
    	this.webTestClient.post()
						.uri(uri)
//						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.exchange()
						.expectStatus()
						.isOk()
						.expectBody()
						.consumeWith(WebTestClientRestDocumentation.document(
								"{class-name}/{method-name}", 
								preprocessRequest(prettyPrint()), 
								preprocessResponse(prettyPrint()), 
								defaultRequestHeader, 
								responseFields(
									fieldWithPath("message")
									.description("API가 기획단계에 있습니다.")
									.attributes(RestDocAttributes.length("Integer.MAX_VALUE"))
								)));
    }
    
    public HttpHeaders getTestHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("OS", "android");
		httpHeaders.add("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJoZXJpdCIsInN1YiI6ImFjZXRrbiIsImF1ZCI6InRlc3QiLCJ0eXAiOiIyIiwiaWF0IjoxNTkzMTMwODAyLCJleHAiOjE1OTQ5NTgxODl9.4xBUAbzdTlRfBhPtM-3Dl3gdLHFDhVuVh_qf6jqtsTY");
		return httpHeaders;
	}

    public ResponseSpec webTestClientPost(String uri, Object request) throws JsonProcessingException, Exception {
    	return webTestClient.post()
							.uri(uri)
//							.accept(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON)
							.body(BodyInserters.fromValue(request))
    						.exchange();
    }
    
    public ResponseSpec webTestClientGet(String uri, Object request) throws JsonProcessingException, Exception {
    	return ((RequestBodySpec) webTestClient.get()
							.uri(uri))
							.contentType(MediaType.APPLICATION_JSON)
							.body(BodyInserters.fromValue(request))
    						.exchange();
    }
}
