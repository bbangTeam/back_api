package io.my.bbang.pilgrimage.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class PilgrimageListDto {
	private String id;
	private String storeName;
	private Boolean isClear;
	private Double latitude;
	private Double longitude;
	
	// optional
	private String imageUrl;
	private String openingHours;
	private String breadName;
	private List<Integer> bakeTimeList;
	
}
