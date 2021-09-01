package io.my.bbang.pilgrimage.repository;

import io.my.bbang.pilgrimage.domain.PilgrimageBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface PilgrimageBoardRepository extends ReactiveCrudRepository<PilgrimageBoard, String> {
    Flux<PilgrimageBoard> findByStoreId(String parentId, Pageable pageable);
    Mono<Long> countByUserId(String userId);



}
