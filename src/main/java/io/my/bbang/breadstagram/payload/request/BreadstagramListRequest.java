package io.my.bbang.breadstagram.payload.request;

import lombok.Data;

@Data
public class BreadstagramListRequest {
	private Integer pageNum;
	private Integer pageSize;
}
