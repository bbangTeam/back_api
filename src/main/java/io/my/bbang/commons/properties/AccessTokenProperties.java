package io.my.bbang.commons.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("jjwt.access-token")
public class AccessTokenProperties {
	private String secretKey;
	private int expiredTime;
}
