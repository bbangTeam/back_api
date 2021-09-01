package io.my.bbang.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RecentlyDto {
    private Result result;

    @Getter @Setter
    public static class Result {
        private FeatureCollection featureCollection;
    }

    @Getter @Setter
    public static class FeatureCollection {
        private List<Feature> features;
    }

    @Getter @Setter
    public static class Feature {
        private Properties properties;
    }

    @Getter @Setter
    public static class Properties {
        private String fullNm;
        private String sigKorNm;
    }
}
