package io.my.bbang.breadstagram.payload.response;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BreadstagramWriteResponse extends BbangResponse {
	private String id;
}
