package io.my.bbang.breadstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.domain.StoreMenu;
import io.my.bbang.breadstore.dto.StoreDTO;
import io.my.bbang.breadstore.repository.StoreMenuRepository;
import io.my.bbang.breadstore.repository.StoreRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StoreService {
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private StoreMenuRepository storeMenuRepository;

	

//	public Flux<Store> storeList(StoreVo bakeryVo) {
//		return storeRepository.findByXposLoAndYposLa(bakeryVo.getXpos_lo(),bakeryVo.getYpos_la());
//	}



	public Flux<Store> storeList(Pageable pageable, StoreDTO storeVo) {
		return storeRepository.findAllByXposLoLikeAndYposLaLike(pageable,storeVo.getXposLo(),storeVo.getYposLa());
	}



	public Flux<Store> storeList(StoreDTO storeVo) {
		return storeRepository.findByXposLoLikeAndYposLaLike(storeVo.getXposLo(),storeVo.getYposLa());
	}

	public Mono<Store> findOneStore(String id) {
		return storeRepository.findById(id);
	}

	public Mono<Store> save(Store store) {
		return storeRepository.save(store);
	}





	public Flux<StoreMenu> findOneStoreMenu(String storeId) {
		return storeMenuRepository.findByStoreId(storeId);
	}

}
