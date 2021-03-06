package io.my.bbang.breadstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.breadstore.domain.Store;
import reactor.core.publisher.Flux;

@Repository
public interface StoreRepository extends ReactiveCrudRepository<Store, String> {

	Flux<Store> findByXposLoLikeAndYposLaLike(double xpos_lo, double ypos_la);

	Flux<Store> findAllByXposLoLikeAndYposLaLike(Pageable pageable, double xposLo, double yposLa);




}
