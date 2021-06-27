package io.my.bbang.breadstagram.payload.request;

import java.util.List;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import lombok.Data;

@Data
public class BreadstagramWriteRequest {	
	private String id;
	private String cityName;
	private String storeName;
	private String breadName;
	private String content;
	private List<BreadstagramImageDto> imageList;

}
