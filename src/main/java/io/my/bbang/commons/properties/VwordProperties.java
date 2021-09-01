package io.my.bbang.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("vword")
public class VwordProperties {
    private String url;
    private String uri;
    private String key;
    private String domain;
    private String commonParams;
}
