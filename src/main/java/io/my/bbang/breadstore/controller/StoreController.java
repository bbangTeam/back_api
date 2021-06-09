package io.my.bbang.breadstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.domain.Vo.StoreVo;
import io.my.bbang.breadstore.service.StoreService;
import io.my.bbang.user.payload.request.UserLoginRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class StoreController {

	@Autowired
	private StoreService storeService;
	
	@GetMapping(path="/map/storelist",produces = "application/json")
	public ResponseEntity<?> stroelist(@RequestBody StoreVo storeVo) {
		Flux<Store> storeList = storeService.storeList(storeVo);
		return new ResponseEntity<>(storeList,HttpStatus.OK);
	}
	@GetMapping(path="/map/store/{id}")
	public ResponseEntity<?> login(@PathVariable String id) {
		Mono<Store> store = storeService.findOneStore(id);
		return new ResponseEntity<>(store,HttpStatus.OK); 
	}
}
