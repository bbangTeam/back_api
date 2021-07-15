package io.my.bbang.pilgrimage.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.pilgrimage.dto.PilgrimageAreaListDto;
import lombok.Data;

@Data
public class PilgrimageAreaListResponse extends BbangResponse {
    private List<PilgrimageAreaListDto> areaList;

    public PilgrimageAreaListResponse() {
        areaList = new ArrayList<>();
    }
}


