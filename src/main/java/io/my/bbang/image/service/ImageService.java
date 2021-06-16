package io.my.bbang.image.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.properties.ImageProperties;
import io.my.bbang.image.payload.UploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageProperties imageProperties;
	
	public Mono<UploadResponse> fileUpload(FilePart filePart) {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", filePart);
		
		MultiValueMap<String, HttpEntity<?>> parts = builder.build();
		
		ResponseSpec responseSpec = WebClient.builder()
							.baseUrl(imageProperties.getBaseUrl())
							.build()
							.post()
							.uri(imageProperties.getUploadUri())
							.bodyValue(parts)
							.retrieve()
		;
		
		return responseSpec.bodyToMono(String.class)
				.map(fileName -> {
					String imageUrl = imageProperties.getBaseUrl() + imageProperties.getDownloadUri() + "/" + fileName;
					
					UploadResponse responseBody = new UploadResponse();
					responseBody.setImageUrl(imageUrl);
					responseBody.setFileName(fileName);
					responseBody.setResult("Success");
					
					return responseBody;
				});
	}
	
	public Mono<BbangResponse> fileDelete(String fileName) {
		ResponseSpec responseSpec = WebClient.builder()
								.baseUrl(imageProperties.getBaseUrl())
								.build()
								.delete()
								.uri(imageProperties.getDeleteUri() + fileName)
								.retrieve()
		;
		
		return responseSpec.bodyToMono(String.class).map(BbangResponse::new);
	}
}
