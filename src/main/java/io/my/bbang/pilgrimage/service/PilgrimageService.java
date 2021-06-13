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
				
				dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
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
