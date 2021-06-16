package io.my.bbang.image.payload;

import lombok.Data;

@Data
public class UploadResponse {
	private String result;
	private String fileName;
	private String imageUrl;
}
