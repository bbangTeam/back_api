package io.my.bbang.image.payload;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;

@Data
public class UploadResponse extends BbangResponse {
	private String id;
	private String fileName;
	private String imageUrl;
}
