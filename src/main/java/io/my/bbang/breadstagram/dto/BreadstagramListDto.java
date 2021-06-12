package io.my.bbang.breadstagram.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreadstagramListDto {
	private String cityName;
	private String breadStoreName;
	private String breadName;
	private Integer like;
	private List<String> tagList;
	private List<String> imageUrlList;
}
