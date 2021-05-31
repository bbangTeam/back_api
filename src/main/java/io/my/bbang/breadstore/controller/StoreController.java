package io.my.bbang.breadstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.service.StoreService;
import reactor.core.publisher.Flux;

@RestController
public class StoreController {

	@Autowired
	private StoreService storeService;
	
	@GetMapping("/storelist")
	public Flux<Store> stroelist() {
		System.out.println("dsf");
		return storeService.storeList();
	}
}
