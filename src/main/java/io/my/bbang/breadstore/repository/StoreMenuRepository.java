package io.my.bbang.breadstore.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.breadstore.domain.StoreMenu;
import reactor.core.publisher.Flux;

@Repository
public interface StoreMenuRepository extends ReactiveCrudRepository<StoreMenu, String>{


	Flux<StoreMenu> findByStoreId(String storeId);

}
