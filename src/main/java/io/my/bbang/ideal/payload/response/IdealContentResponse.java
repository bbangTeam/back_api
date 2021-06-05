package io.my.bbang.ideal.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.ideal.dto.IdealContentDto;
import lombok.Data;

@Data
public class IdealContentResponse {
	private String result;
	private List<IdealContentDto> breadList;
	
	public IdealContentResponse() {
		breadList = new ArrayList<>();
	}

}
