package io.my.bbang.pilgrimage.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.pilgrimage.domain.Pilgrimage;
import reactor.core.publisher.Flux;

@Repository
public interface PilgrimageRepository extends ReactiveCrudRepository<Pilgrimage, String>{
    Flux<Pilgrimage> findAllByPilgrimageAreaId(String pilgrimageAreaId);
}
