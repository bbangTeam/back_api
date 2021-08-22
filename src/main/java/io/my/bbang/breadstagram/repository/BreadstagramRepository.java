package io.my.bbang.breadstagram.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.breadstagram.domain.Breadstagram;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BreadstagramRepository extends ReactiveCrudRepository<Breadstagram, String> {

    Flux<Breadstagram> findByIdNotNull(Pageable pageable);
    Mono<Long> countAllByUserId(String userId);
    Mono<Long> countAllByStoreId(String storeId);

}
