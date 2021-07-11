package io.my.bbang.commons.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
	private final AuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		return Mono.empty();
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			
			return authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(jwt, jwt))
					.map(SecurityContextImpl::new);
		}
		
		return Mono.error(new BbangException(ExceptionTypes.AUTH_EXCEPTION));
	}
	

}
