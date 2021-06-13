package io.my.bbang.ideal.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.my.bbang.ideal.dto.IdealContentDto;
import io.my.bbang.ideal.payload.response.IdealContentResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IdealService {
	
	public Mono<IdealContentResponse> content() {
		IdealContentResponse responseBody = new IdealContentResponse();
		
		responseBody.setResult("Success");
		
		for (int i=0; i<32; i++) {
			IdealContentDto dto = new IdealContentDto();
			dto.setId(UUID.randomUUID().toString());
			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			dto.setName("bread" + i);
			responseBody.getBreadList().add(dto);
		}
		
		return Mono.just(responseBody);
	}

}
