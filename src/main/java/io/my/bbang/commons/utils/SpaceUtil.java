package io.my.bbang.commons.utils;

import io.my.bbang.commons.properties.VwordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SpaceUtil {
    private final VwordProperties vwordProperties;
    private static final String SIG_CD = "&attrFilter=sig_cd:=:";

    public static final double R = 6372.8;

    // 출처: https://rosettacode.org/wiki/Haversine_formula#Java
    // 저작권 정책: GNU Free Documentation License 1.2
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    public String getSigCdUrl(int code) {
        return getSigCdUrl(code + "");
    }

    public String getSigCdUrl(String code) {
        return vwordProperties.getUrl()
                + vwordProperties.getUri()
                + vwordProperties.getCommonParams()
                + "&key="
                + vwordProperties.getKey()
                + "&domain="
                + vwordProperties.getDomain()
                + SIG_CD
                + code
                ;
    }

}
