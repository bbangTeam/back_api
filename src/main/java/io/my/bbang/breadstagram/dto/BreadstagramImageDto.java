package io.my.bbang.breadstagram.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class BreadstagramImageDto {
	private String id;
	private Integer num;
	private String imageUrl;

}
