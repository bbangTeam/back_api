package io.my.bbang.commons.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.my.bbang.commons.context.ReactiveJwtContextHolder;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
	private final JwtUtil jwtUtil;
	private final UserService userService;
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String jwt = authentication.getCredentials().toString();
		
		if ( !jwtUtil.verifyAccessToken(jwt) ) {
			return Mono.empty();
		}
		String userId = jwtUtil.getUserIdByAccessToken(jwt);
		Mono<User> entity = userService.findById(userId);

		entity.subscribe();

		return entity.map(user -> {
			ReactiveJwtContextHolder.getContext().map(context -> {
				context.setUser(user);
				return context;
			});

			return (Authentication) new UsernamePasswordAuthenticationToken(
							user.getUsername(), 
							user.getPassword(), 
							user.getAuthorities());
		}).switchIfEmpty(Mono.error(new BbangException("authenticate exception!")));
	}

}
