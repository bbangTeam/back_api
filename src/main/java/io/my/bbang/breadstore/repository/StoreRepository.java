package io.my.bbang.breadstore.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.breadstore.domain.Store;
import reactor.core.publisher.Flux;

@Repository
public interface StoreRepository extends ReactiveCrudRepository<Store, String> {

	@Query("{ location: { $nearSphere : { $geometry : { type : 'Point', coordinates : [?#{[0]}, ?#{[1]}] }, $minDistance : ?#{[2]}, $maxDistance : ?#{[3]} }}}")
	Flux<Store> findByLocation(double xposLo, double yposLa, int minDistance, int maxDistance);


}
