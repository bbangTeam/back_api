package io.my.bbang.breadstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.domain.StoreMenu;
import io.my.bbang.breadstore.dto.StoreDTO;
import io.my.bbang.breadstore.service.StoreService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class StoreController {

	@Autowired
	private StoreService storeService;
	
	@GetMapping(path="/map/storelist")
	public ResponseEntity<?> stroelist(@RequestBody StoreDTO storeVo,Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
		Flux<Store> storeList = storeService.storeList(pageable,storeVo); 
		 
		return new ResponseEntity<>(storeList,HttpStatus.OK);
		//return new ResponseEntity<>(new StoreDTO(storeList),HttpStatus.OK);
	}
	@GetMapping(path="/map/store/{id}")
	public ResponseEntity<?> login(@PathVariable String id) {
		Mono<Store> store = storeService.findOneStore(id);
		Flux<StoreMenu> storeMenu = storeService.findOneStoreMenu(store.block().getStoreId());
		return new ResponseEntity<>(new StoreDTO(store,storeMenu),HttpStatus.OK); 
	}
	
}
