package io.my.bbang.commons.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.my.bbang.commons.base.SpringBootTestBase;

@Slf4j
class JwtUtilTests extends SpringBootTestBase {

	@Test
	@DisplayName("Access토큰 생성 성공 테스트")
	void createAccessTokenTest() {
		String accessToken = jwtUtil.createAccessToken("60e83462d9fe745685375ba9");

		log.info(accessToken);

		assertNotNull(accessToken);
	}
	
	@Test
	@DisplayName("생성된 AccessToken의 userId가 정확히 입력되는지 확인하기 위한 테스트")
	void getUserIdByAccessToken() {
		String userId = "userId";
		String accessToken = jwtUtil.createAccessToken(userId);
		
		assertEquals(userId, jwtUtil.getUserIdByAccessToken(accessToken));
	}
	
	@Test
	@DisplayName("Refresh토큰 생성 성공 테스트")
	void createRefreshTokenTest() {
		String refreshToken = jwtUtil.createRefreshToken("userId");
		
		assertNotNull(refreshToken);
	}
	
	@Test
	@DisplayName("AccessToken 파싱 성공 테스트")
	void parseAccessToken() {
		String accessToken = jwtUtil.createAccessToken("userId");
		Claims claims = jwtUtil.parseAccessToken(accessToken);
		
		assertTrue(claims.getExpiration().getTime() > new Date().getTime());
	}
	
	@Test
	@DisplayName("RefreshToken 파싱 성공 테스트")
	void parseRefreshToken() {
		String refreshToken = jwtUtil.createRefreshToken("userId");
		Claims claims = jwtUtil.parseRefreshToken(refreshToken);
		
		assertTrue(claims.getExpiration().getTime() > new Date().getTime());
	}

	@Test
	@DisplayName("AccessToken 확인 체크 성공 테스트")
	void verifyAccessToken() {
		String accessToken = jwtUtil.createAccessToken("userId");
		
		assertTrue(jwtUtil.verifyAccessToken(accessToken));
	}
	
	@Test
	@DisplayName("RefreshToken 확인 체크 성공 테스트")
	void verifyRefreshToken() {
		String refreshToken = jwtUtil.createRefreshToken("userId");
		
		assertTrue(jwtUtil.verifyRefreshToken(refreshToken));
	}

	

}
