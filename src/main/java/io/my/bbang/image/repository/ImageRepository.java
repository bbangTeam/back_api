package io.my.bbang.image.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.image.domain.Image;

@Repository
public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
    
}
