package io.my.bbang.area.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.area.payload.response.AreaListResponse;
import io.my.bbang.area.service.AreaService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/area")
public class AreaController {
    private final AreaService areaService;

    @GetMapping("/list")
    public Mono<AreaListResponse> areaList() {
        return areaService.areaList();
    }


    
}
