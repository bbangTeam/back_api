package io.my.bbang.ideal.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.ideal.dto.IdealContentDto;
import io.my.bbang.ideal.payload.response.IdealResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IdealService {
	
	public Mono<IdealResponse> content() {
		IdealResponse responseBody = new IdealResponse();
		
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

	public Mono<IdealResponse> rank() {
		IdealResponse responseBody = new IdealResponse();

		responseBody.setResult("Success");

		for (int i=0; i<5; i++) {
			IdealContentDto dto = new IdealContentDto();
			dto.setName("bread" + i);
			dto.setImageUrl("https://t1.daumcdn.net/liveboard/dailylife/16886ca4df48462e911cfac9bf434434.JPG");
			responseBody.getBreadList().add(dto);
		}

		return Mono.just(responseBody);
	}

	public Mono<BbangResponse> selected(String id) {
		return Mono.just(new BbangResponse("Success"));
	}

}
