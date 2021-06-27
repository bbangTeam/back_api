package io.my.bbang.breadstagram.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.breadstagram.dto.BreadstagramListDto;
import io.my.bbang.breadstagram.payload.request.BreadstagramWriteRequest;
import io.my.bbang.breadstagram.payload.response.BreadstagramListResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramViewResponse;
import io.my.bbang.breadstagram.payload.response.BreadstagramWriteResponse;
import io.my.bbang.commons.payloads.BbangResponse;
import reactor.core.publisher.Mono;

@Service
public class BreadstagramService {

	public Mono<BreadstagramListResponse> list(int pageNum, int pageSize) {
		BreadstagramListResponse responseBody = new BreadstagramListResponse();
		responseBody.setResult("Success");
		
		for (int i=0; i<pageSize; i++) {
			BreadstagramListDto dto = new BreadstagramListDto();
			
			List<String> imageUrlList = new ArrayList<>();
			
			for (int index=0; index<(int)((Math.random()*10000)%10); index++) {
				imageUrlList.add("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			}
			
			dto.setBreadName("bread" + i);
			dto.setBreadStoreName("breadStore" + i);
			dto.setCityName("Seoul");
			dto.setImageUrlList(imageUrlList);
			dto.setLike((int)((Math.random()*100000)));
			
			
			dto.setId("bread-stagram-id" + i);
			responseBody.getBreadstagramList().add(dto);
		}
		
		
		return Mono.just(responseBody);
	}
	
	public Mono<BreadstagramViewResponse> view(String id) {
		BreadstagramViewResponse responseBody = new BreadstagramViewResponse();
		
		responseBody.setResult("Success");
		responseBody.setBreadName("소보루");
		responseBody.setCityName("서울");
		responseBody.setNickname("빵터짐");
		responseBody.setStoreName("빵터짐 1호점");
		responseBody.setLike((int)((Math.random()*100000)));
		
		List<BreadstagramImageDto> imageList = new ArrayList<>();
		

		for (int index=0; index<(int)((Math.random()*10000)%10 + 1); index++) {
			BreadstagramImageDto dto = new BreadstagramImageDto();
			dto.setId(UUID.randomUUID().toString());
			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			dto.setNum(index);
			imageList.add(dto);
		}
		
		responseBody.setImageList(imageList);
		
		return Mono.just(responseBody);
	}
	
	public Mono<BreadstagramWriteResponse> write(BreadstagramWriteRequest requestBody) {
		BreadstagramWriteResponse responseBody = new BreadstagramWriteResponse();
		
		responseBody.setId(UUID.randomUUID().toString());
		responseBody.setResult("Success");
		
		return Mono.just(responseBody);
	}

	public Mono<BbangResponse> like(Boolean like) {
		BbangResponse responseBody = new BbangResponse("Success");
		return Mono.just(responseBody);
	}
		

}
