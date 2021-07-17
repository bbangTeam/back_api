package io.my.bbang.ideal.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.ideal.domain.Ideal;
import reactor.core.publisher.Flux;

@Repository
public interface IdealRepository extends ReactiveCrudRepository<Ideal, String> {

    Flux<Ideal> findByIdNotNull(Pageable pageable);
    
}
