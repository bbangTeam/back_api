package io.my.bbang.commons.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.my.bbang.commons.context.ReactiveJwtContextHolder;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.utils.JwtUtil;
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
		
		if ( jwt == null || !jwtUtil.verifyAccessToken(jwt) ) {
			// throw new BbangException(ExceptionTypes.AUTH_EXCEPTION);
		}

		String userId = jwtUtil.getUserIdByAccessToken(jwt);

		return userService.findById(userId).map(user -> {
			ReactiveJwtContextHolder.getContext().map(context -> {
				context.setUser(user);
				return context;
			});

			return (Authentication) new UsernamePasswordAuthenticationToken(
							user.getUsername(), 
							user.getPassword(), 
							user.getAuthorities());
		})
		// .switchIfEmpty(Mono.error(new BbangException("authenticate exception!")))
		;
	}

}
