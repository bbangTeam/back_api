package io.my.bbang.breadstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.domain.Vo.StoreVo;
import io.my.bbang.breadstore.service.StoreService;
import reactor.core.publisher.Flux;

@RestController
public class StoreController {

	@Autowired
	private StoreService storeService;
	
	@GetMapping(path="/map/storelist",produces = "application/json")
	public ResponseEntity<?> stroelist(@RequestBody StoreVo storeVo) {
		Flux<Store> storeList = storeService.storeList(storeVo);
		return new ResponseEntity<>(storeList,HttpStatus.OK);
	}
}
