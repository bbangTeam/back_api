package io.my.bbang.commons.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.my.bbang.commons.context.JwtContextHolder;
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
			return Mono.empty();
		}

		String userId = jwtUtil.getUserIdByAccessToken(jwt);

		return userService.findById(userId).map(user -> {
			JwtContextHolder.getContext().subscribe(context -> context.setUser(user));

			return new UsernamePasswordAuthenticationToken(
					user.getUsername(),
					user.getPassword(),
					user.getAuthorities());
		})
		;

	}

}
