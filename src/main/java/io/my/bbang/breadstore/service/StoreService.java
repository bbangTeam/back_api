package io.my.bbang.breadstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.repository.StoreRepository;
import reactor.core.publisher.Flux;

@Service
public class StoreService {
	
	@Autowired
	private StoreRepository storeRepository;

	public Flux<Store> storeList() {
		return storeRepository.findAll();
	}

}
