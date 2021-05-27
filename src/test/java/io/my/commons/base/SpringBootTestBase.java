package io.my.commons.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.my.bbang.test.repository.TestRepository;
import io.my.bbang.test.service.TestService;
import io.my.commons.properties.AccessTokenProperties;
import io.my.commons.properties.RefreshTokenProperties;
import io.my.commons.utils.JwtUtil;

@SpringBootTest
public class SpringBootTestBase extends TestBase {
	
	@Autowired
	protected JwtUtil jwtUtil;
	
	@Autowired
	protected TestService testService;

	@Autowired
	protected TestRepository testRepository;

	@Autowired
	protected AccessTokenProperties accessTokenProperties;
	
	@Autowired
	protected RefreshTokenProperties refreshTokenProperties;
	
}
