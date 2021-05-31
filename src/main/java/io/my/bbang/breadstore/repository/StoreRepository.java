package io.my.bbang.breadstore.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.breadstore.domain.Store;

@Repository
public interface StoreRepository extends ReactiveCrudRepository<Store, String> {


}
