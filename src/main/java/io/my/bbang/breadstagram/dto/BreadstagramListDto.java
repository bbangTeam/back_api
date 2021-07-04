package io.my.bbang.breadstagram.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreadstagramListDto {
	private String id;
	private String storeId;
	private String cityName;
	private String breadStoreName;
	private String breadName;
	private Integer like;
	private List<String> imageUrlList;
	private List<BreadstagramImageDto> imageList;
}
