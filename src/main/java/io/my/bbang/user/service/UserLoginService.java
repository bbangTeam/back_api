package io.my.bbang.user.service;

import io.my.bbang.user.service.oauth.SocialOauthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.security.UserRole;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.payload.response.UserJoinResponse;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private List<SocialOauthService> socialOauthServiceList;
    
	public Mono<UserJoinResponse> join(String email, String nickname, String accessToken) {
		log.info("join service");
		
//		User user = User.newInstance(loginId, passwordEncoder.encode(password));
//		user.setName(name);
//		user.getRoles().add?(UserRole.ROLE_USER);
		
//		return userRepository.save(user).map(this::returnJoinResponse);
		return null;
	}

	private UserJoinResponse returnJoinResponse(User entity) {
		UserJoinResponse responseBody = new UserJoinResponse();
		responseBody.setId(entity.getId());
		responseBody.setLoginId(entity.getLoginId());
		responseBody.setCreateTime(entity.getCreateDate());
		return responseBody;
	}

    
}
