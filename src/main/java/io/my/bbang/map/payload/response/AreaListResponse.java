package io.my.bbang.map.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.map.dto.AreaListDto;
import lombok.Data;

@Data
public class AreaListResponse {
    private List<AreaListDto> areaList;
    private String result;

    public AreaListResponse() {
        areaList = new ArrayList<>();
    }
}
