package io.my.bbang.commons.base;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.request.RequestPartsSnippet;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters.MultipartInserter;

import io.my.bbang.breadstagram.service.BreadstagramService;
import io.my.bbang.breadstore.service.StoreService;
import io.my.bbang.comment.service.CommentService;
import io.my.bbang.commons.security.SecurityContextRepository;
import io.my.bbang.commons.service.JwtService;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.ideal.service.IdealService;
import io.my.bbang.image.service.ImageService;
import io.my.bbang.pilgrimage.service.PilgrimageService;
import io.my.bbang.user.repository.UserRepository;
import reactor.core.publisher.Mono;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RestDocsBaseWithSpringBoot extends TestBase {
	protected String authorization;

	protected WebTestClient webTestClient;
	protected Snippet defaultRequestHeader;
	
    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    protected UserRepository testRepository;

	@Autowired
	protected JwtUtil jwtUtil;
    
    @MockBean
    protected ImageService imageService;

	@MockBean
	protected IdealService idealService;
    
	@MockBean
	protected BreadstagramService breadstagramService;

	@MockBean
	protected CommentService commentService;

	@MockBean
	protected StoreService storeService;

	@MockBean
	protected PilgrimageService pilgrimageService;

	@MockBean
	protected JwtService jwtService;

	@MockBean
	private SecurityContextRepository securityContextRepository;

	@BeforeEach
	void setUp(ApplicationContext applicationContext,
			RestDocumentationContextProvider restDocumentation) {

		authorization = jwtUtil.createAccessToken("id");
		Mockito.when(securityContextRepository.load(Mockito.any()))
		.thenReturn(
			Mono.just(
				(Authentication) new UsernamePasswordAuthenticationToken(
				"username", 
				"userPassword", 
				new ArrayList<>())).map(SecurityContextImpl::new)
		);

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
	
	protected ResponseSpec postWebTestClient(MultipartInserter multipartInserter, String uri) {
		return this.webTestClient.post().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).body(multipartInserter).exchange();
	}
	
	protected ResponseSpec postWebTestClient(String uri) {
		return this.webTestClient.post().uri(uri).accept(MediaType.APPLICATION_JSON).exchange();
	}
	
	protected ResponseSpec putWebTestClient(Object body, String uri) {
		return this.webTestClient.put().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
	}
	
	protected ResponseSpec deleteWebTestClient(String uri) {
		return this.webTestClient.delete().uri(uri).header(HttpHeaders.AUTHORIZATION, authorization).accept(MediaType.APPLICATION_JSON).exchange();
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
	
	protected Consumer<EntityExchangeResult<byte[]>> createConsumer(
			String fileName, 
			RequestPartsSnippet requestPartsSnippet, 
			ResponseFieldsSnippet responseSnippet) {
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				requestPartsSnippet, 
				responseSnippet);
	}

	protected Consumer<EntityExchangeResult<byte[]>> createConsumerAuthorization(
			String fileName, 
			ResponseFieldsSnippet responseSnippet) {

		Snippet responseHeader = responseHeaders(
			headerWithName("Authorization")
				.description("갱신된 JWT 인증 토큰")
				.optional()
				.attributes(
						RestDocAttributes.length(""),
						RestDocAttributes.format("")
			)
		);
			
		return document(
				this.getClass().getSimpleName().toLowerCase() + fileName, 
				preprocessRequest(prettyPrint()), 
				preprocessResponse(prettyPrint()), 
				defaultRequestHeader, 
				responseHeader,
				responseSnippet);
	}

	
}
