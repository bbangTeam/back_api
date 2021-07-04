package io.my.bbang.area.service;

import org.springframework.stereotype.Service;

import io.my.bbang.area.dto.AreaListDto;
import io.my.bbang.area.payload.response.AreaListResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class AreaService {

    public Mono<AreaListResponse> areaList() {
        AreaListResponse responseBody = new AreaListResponse();
        
        for (int i=0; i<7; i++) {
            responseBody.getAreaList().add(new AreaListDto("id" + i,"name" + i));
        }
        responseBody.setResult("Success");

        return Mono.just(responseBody);
    }

    
    
}
