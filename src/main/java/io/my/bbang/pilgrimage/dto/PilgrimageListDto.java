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
	private boolean clear;
	private Double latitude;
	private Double longitude;
	private long commentCount;
	private long likeCount;
	private long clickCount;
	private boolean isLike;
	
	// optional
	private String imageUrl;
	private String openingHours;
	private String breadName;
	private List<Integer> bakeTimeList;

	// 서비스에서 함수 안에 접근하기 위한 값으로, 응답값으로 불필요함
	private String pilgrimageId;
	
}
