package io.my.bbang.ideal.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.ideal.dto.IdealContentDto;
import lombok.Data;

@Data
public class IdealResponse extends BbangResponse {
	private List<IdealContentDto> breadList;
	
	public IdealResponse() {
		breadList = new ArrayList<>();
	}

}
