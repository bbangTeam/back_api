package io.my.bbang.ideal.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.ideal.dto.IdealRankDto;
import lombok.Data;

@Data
public class IdealRankResponse extends BbangResponse {
    private List<IdealRankDto> breadList;

    public IdealRankResponse() {
        breadList = new ArrayList<>();
    }
    
}
