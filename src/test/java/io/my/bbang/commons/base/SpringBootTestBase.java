package io.my.bbang.commons.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.my.bbang.commons.properties.AccessTokenProperties;
import io.my.bbang.commons.properties.RefreshTokenProperties;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.repository.UserRepository;
import io.my.bbang.user.service.UserService;

@SpringBootTest
public class SpringBootTestBase extends TestBase {
	
	@Autowired
	protected JwtUtil jwtUtil;
	
	@Autowired
	protected UserService testService;

	@Autowired
	protected UserRepository testRepository;

	@Autowired
	protected AccessTokenProperties accessTokenProperties;
	
	@Autowired
	protected RefreshTokenProperties refreshTokenProperties;
	
}
