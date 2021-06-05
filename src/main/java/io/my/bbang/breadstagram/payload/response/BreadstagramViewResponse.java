package io.my.bbang.breadstagram.payload.response;

import java.util.List;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import lombok.Data;

@Data
public class BreadstagramViewResponse {
	private String result;
	private String cityName;
	private String storeName;
	private String breadName;
	private String nickname;
	private Integer like;
	private List<String> tagList;
	private List<BreadstagramImageDto> imageList;
}
