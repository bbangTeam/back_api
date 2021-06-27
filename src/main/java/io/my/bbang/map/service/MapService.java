package io.my.bbang.map.service;

import org.springframework.stereotype.Service;

import io.my.bbang.map.dto.AreaListDto;
import io.my.bbang.map.payload.response.AreaListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapService {

    public Mono<AreaListResponse> areaList() {
        AreaListResponse responseBody = new AreaListResponse();
        
        for (int i=0; i<7; i++) {
            responseBody.getAreaList().add(new AreaListDto("id" + i,"name" + i));
        }
        responseBody.setResult("Success");

        return Mono.just(responseBody);
    }

    
    
}
