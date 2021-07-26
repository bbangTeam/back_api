package io.my.bbang.commons.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {
	private final SecurityContextRepository securityContextRepository;
	
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http.exceptionHandling()
					.authenticationEntryPoint(
							(swe, e) -> Mono.fromRunnable(
									() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
					.accessDeniedHandler(
							(swe, e) -> Mono.fromRunnable(
									() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
					.and()
					.formLogin().disable()
					.httpBasic().disable()
					.securityContextRepository(securityContextRepository)
					.authorizeExchange()
					.pathMatchers(HttpMethod.OPTIONS).permitAll()
					.pathMatchers("/api/specification/**").permitAll()
					.pathMatchers("/api/healthcheck").permitAll()
					.pathMatchers("/api/oauth/**").permitAll()
					.pathMatchers("/api/**").authenticated()
					.pathMatchers("/**").permitAll()
					.and()
					.csrf().disable()
					.build();
	}
	
}
