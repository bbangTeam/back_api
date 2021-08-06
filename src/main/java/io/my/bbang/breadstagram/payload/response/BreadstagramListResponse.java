package io.my.bbang.breadstagram.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BreadstagramListResponse extends BbangResponse {
	private List<BreadstagramListDto> breadstagramList;
	
	public BreadstagramListResponse() {
		breadstagramList = new ArrayList<>();
	}
}
