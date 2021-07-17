package io.my.bbang.commons.utils;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.my.bbang.commons.context.JwtContextHolder;
import io.my.bbang.commons.properties.AccessTokenProperties;
import io.my.bbang.commons.properties.RefreshTokenProperties;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtUtil {
	private final AccessTokenProperties accessTokenProperties;
	private final RefreshTokenProperties refreshTokenProperties;
	
	public String createAccessToken(String userId) {
		return createToken(
				userId, 
				accessTokenProperties.getSecretKey(), 
				accessTokenProperties.getExpiredTime());
	}
	
	public String createRefreshToken(String userId) {
		return createToken(
				userId, 
				refreshTokenProperties.getSecretKey(), 
				refreshTokenProperties.getExpiredTime());
	}
	
	private String createToken(String userId, String secretKey, int expiredTime) {
		return createToken(
				userId, 
				getKey(secretKey), 
				Timestamp.valueOf(LocalDateTime.now().plusHours(expiredTime)))
		;
	}
	
	private String createToken(String userId, Key key, Timestamp expiredTime) {
		String subject = "user";
		Map<String, Object> headers = new HashMap<>();
		Map<String, Object> payloads = new HashMap<>();
		
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		payloads.put("userId", userId);
		
        return Jwts.builder()
	                .setHeader(headers)
	                .setClaims(payloads)
	                .setSubject(subject)
	                .setExpiration(expiredTime)
	                .signWith(SignatureAlgorithm.HS512, key)
	                .compact();
	}
	
	public String getUserIdByAccessToken(String jwt) {
		return parseAccessToken(jwt).get("userId").toString();
	}

	public Claims parseAccessToken(String jwt) {
		return parseJwt(jwt, getKey(accessTokenProperties.getSecretKey()));
	}
	
	public boolean verifyAccessToken(String jwt) {
		return parseAccessToken(jwt).getExpiration().after(new Date());
	}
	
	public boolean verifyRefreshToken(String jwt) {
		return parseRefreshToken(jwt).getExpiration().after(new Date());
	}

	public Claims parseRefreshToken(String jwt) {
		return parseJwt(jwt, getKey(refreshTokenProperties.getSecretKey()));
	}

	private Claims parseJwt(String jwt, Key key) {
		return Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(jwt)
					.getBody();
	}
	
	private Key getKey(String secretKey) {
		byte[] secretBytes = Base64.getEncoder().encode(secretKey.getBytes());
		return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS512.getJcaName());
	}

	/**
	 * ReactiveJwtContextHolder에서 jwt 정보를 가져와서 userId 반환.
	 */
	public Mono<String> getMonoUserId() {
		return JwtContextHolder.getContext()
		.flatMap(context -> {
			String userId = context.getUserId();
			if (userId == null) {
				return context.getJwt().map(jwt -> getUserIdByAccessToken(jwt));
			} else {
				return Mono.just(userId);
			}
		});
	}

}
