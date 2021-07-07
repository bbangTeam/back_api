package io.my.bbang.pilgrimage.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import io.my.bbang.pilgrimage.domain.PilgrimageAddress;

public interface PilgrimageAddressRepository extends ReactiveCrudRepository<PilgrimageAddress, String> {
    
}
