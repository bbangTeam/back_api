package io.my.bbang.breadstagram.payload.response;

import java.util.List;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;

@Data
public class BreadstagramViewResponse extends BbangResponse {
	private String cityName;
	private String storeName;
	private String breadName;
	private String nickname;
	private Integer like;
	private Integer code;
	private List<BreadstagramImageDto> imageList;
}
