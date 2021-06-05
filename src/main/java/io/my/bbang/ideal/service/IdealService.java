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
			dto.setImageUrl("https://www.google.com/search?q=%EB%B9%B5&sxsrf=ALeKk00wsfd4UWtwx6an9jQfHmnNkqD6Ww:1622848627911&tbm=isch&source=iu&ictx=1&fir=BUqLTYC8yRrGoM%252CwHnrnQYFPg0S7M%252C_&vet=1&usg=AI4_-kQDofV6uK_7vvIYREk_UFRP3l-wcQ&sa=X&ved=2ahUKEwjSkPafjv_wAhVWMd4KHex5A5sQ_h16BAgtEAE#imgrc=BUqLTYC8yRrGoM");
			dto.setName("bread" + i);
			responseBody.getBreadList().add(dto);
		}
		
		return Mono.just(responseBody);
	}

}
