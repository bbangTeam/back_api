package io.my.bbang.breadstagram.dto;

import java.util.List;

import lombok.Data;

@Data
public class BreadstagramListDto {
	private String cityName;
	private String breadStoreName;
	private String breadName;
	private Integer like;
	private List<String> tagList;
	private List<String> imageUrlList;
}
