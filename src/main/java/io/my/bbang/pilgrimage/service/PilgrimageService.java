package io.my.bbang.pilgrimage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import io.my.bbang.pilgrimage.payload.response.PilgrimageListResponse;
import io.my.bbang.pilgrimage.repository.PilgrimageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PilgrimageService {
	private final ModelMapper modelMapper;
	private final PilgrimageRepository pilgrimageRepository;

	public Mono<PilgrimageListResponse> list(String id, String option) {
		PilgrimageListResponse responseBody = new PilgrimageListResponse();

		log.info("call pilgrimage list service!!!");

		responseBody.setResult("Success");
		responseBody.setCityName("서울");
		
		for (int i=0; i<15; i++) {
			PilgrimageListDto dto = new PilgrimageListDto();
			dto.setStoreName("bakery" + i);
			dto.setId(UUID.randomUUID().toString());
			dto.setIsClear(i%2==0);
			dto.setLatitude(37.555107 + (i / 1000d));
			dto.setLongitude(126.970691 + (i / 1000d));
			
			if (option.equals("all")) {
				List<Integer> bakeTimeList = new ArrayList<>();
				
				dto.setImageUrl("https://www.google.com/imgres?imgurl=https%3A%2F%2Fcloudfront-ap-northeast-1.images.arcpublishing.com%2Fchosunbiz%2FAVZUDXPTLSFWAD4BKFXAFOW7VQ.jpg&imgrefurl=https%3A%2F%2Fbiz.chosun.com%2Fsite%2Fdata%2Fhtml_dir%2F2019%2F04%2F16%2F2019041602279.html&tbnid=v1IIKf0utHD_JM&vet=12ahUKEwjVk9eKh__wAhVqQfUHHTziAv0QMygCegUIARDNAQ..i&docid=yPgI60UYYlXmXM&w=522&h=261&q=%EB%B9%B5%EC%A7%91&ved=2ahUKEwjVk9eKh__wAhVqQfUHHTziAv0QMygCegUIARDNAQ");
				dto.setOpeningHours("07 ~ 20");
				dto.setBreadName("소보루");
				dto.setBakeTimeList(bakeTimeList);
				
				for (int j=8; j<20; j+=2) {
					bakeTimeList.add(j);
				}
			}
			
			responseBody.getStoreList().add(dto);
		}
		
		return Mono.just(responseBody);
		
		
//		Mono<Pilgrimage> pilgrimageDocument = pilgrimageRepository.findById(id);
//
//		pilgrimageDocument.hasElement().subscribe(hasElement -> responseBody.setResult("Success"));
//
//		pilgrimageDocument.subscribe(document -> {
//			responseBody.setCityName(document.getName());
//
//			document.getBreadStoreList().forEach(vo -> {
//				PilgrimageListDto dto = null;
//				
//				if (options.equals("all")) {
//					dto = modelMapper.map(vo, PilgrimageListDto.class);
//				} else if (options.equals("none")) {
//					
//				}
//				responseBody.getBakeryList().add(dto);
//			});
//		});
//
//		return Mono.just(responseBody);
	}
}
