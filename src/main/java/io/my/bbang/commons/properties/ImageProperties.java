package io.my.bbang.commons.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("image")
public class ImageProperties {
	private String baseUrl;
	private String uploadUri;
	private String downloadUri;
	private String deleteUri;
}
