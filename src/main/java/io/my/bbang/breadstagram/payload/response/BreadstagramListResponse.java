package io.my.bbang.breadstagram.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import lombok.Data;

@Data
public class BreadstagramListResponse {
	private String result;
	private List<BreadstagramListDto> breadstagramList;
	
	public BreadstagramListResponse() {
		breadstagramList = new ArrayList<>();
	}
}
