package io.my.bbang.breadstore.repository;

import io.my.bbang.breadstore.domain.StoreBread;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StoreBreadRepository extends ReactiveCrudRepository<StoreBread, String> {

    Flux<StoreBread> findByStoreId(String storeId);


}
