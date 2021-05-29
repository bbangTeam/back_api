package io.my.bbang.commons.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jjwt.refresh-token")
public class RefreshTokenProperties {
	private String secretKey;
	private int expiredTime;
}
