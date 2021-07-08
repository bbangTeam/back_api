package io.my.bbang.code.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.code.domain.Code;
import reactor.core.publisher.Flux;

@Repository
public interface CodeRepository extends ReactiveCrudRepository<Code, String> {
    Flux<Code> findAllByCodes(String codes);
}
