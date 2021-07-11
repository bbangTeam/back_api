package io.my.bbang.image.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.properties.ImageProperties;
import io.my.bbang.image.domain.Image;
import io.my.bbang.image.payload.UploadResponse;
import io.my.bbang.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageProperties imageProperties;
	private final ImageRepository imageRepository;
	
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
				.flatMap(fileName -> {
					String imageUrl = imageProperties.getBaseUrl() + imageProperties.getDownloadUri() + "/" + fileName;
					Image image = Image.build(imageUrl, fileName);
					return imageRepository.save(image);
				})
				.map(this::returnResponse);
	}

	private UploadResponse returnResponse(Image image) {
		UploadResponse responseBody = new UploadResponse();
		responseBody.setId(image.getId());
		responseBody.setImageUrl(image.getUrl());
		responseBody.setFileName(image.getFileName());
		responseBody.setResult("Success");
		return responseBody;

	}
	
	public Mono<BbangResponse> fileDelete(String id, String fileName) {
		ResponseSpec responseSpec = WebClient.builder()
								.baseUrl(imageProperties.getBaseUrl())
								.build()
								.delete()
								.uri(imageProperties.getDeleteUri() + fileName)
								.retrieve()
		;
		
		responseSpec.bodyToMono(String.class).subscribe();
		imageRepository.deleteById(id).subscribe();
		return Mono.just(new BbangResponse("Success"));
	}

	public Mono<Image> save(Image image) {
		return imageRepository.save(image);		
	}

	public Flux<Image> saveAll(List<Image> imageList) {
		return imageRepository.saveAll(imageList);
	}

}
