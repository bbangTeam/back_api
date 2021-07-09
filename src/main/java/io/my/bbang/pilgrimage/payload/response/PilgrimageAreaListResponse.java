package io.my.bbang.pilgrimage.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.pilgrimage.dto.PilgrimageAreaListDto;
import lombok.Data;

@Data
public class PilgrimageAreaListResponse {
    private List<PilgrimageAreaListDto> areaList;
    private String result;

    public PilgrimageAreaListResponse() {
        areaList = new ArrayList<>();
    }
}


