package io.my.bbang.ideal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdealContentDto {
	private String id;
	private String name;
	private String imageUrl;
}
