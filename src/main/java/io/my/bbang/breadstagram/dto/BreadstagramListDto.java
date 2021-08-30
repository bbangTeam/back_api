package io.my.bbang.breadstagram.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreadstagramListDto {
	private String id;
	private String storeId;
	private String cityName;
	private String breadStoreName;
	private String content;
	private String nickname;
	private boolean isLike;
	private long commentCount;
	private long likeCount;
	private long clickCount;
	private LocalDateTime modifyDate;
	private LocalDateTime createDate;
	private List<String> imageUrlList;
	private List<String> breadNameList;
	private List<BreadstagramImageDto> imageList;

	private double star;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userId;
}
