package io.my.bbang.commons.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.test.entity.TestEntity;
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
		
		try {
			if ( !jwtUtil.verifyAccessToken(jwt) ) {
				return Mono.empty();
			}
			String userId = jwtUtil.getUserIdByAccessToken(jwt);
			Mono<TestEntity> entity = userService.findById(userId);
			
			return entity.map(user -> {
				List<GrantedAuthority> authorities = new ArrayList<>();
				user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
				
				return new UsernamePasswordAuthenticationToken(
								user.getLoginId(), 
								user.getPassword(), 
								authorities);
			});
			
		} catch (Exception e) {
			return Mono.empty();
		}
	}

}
