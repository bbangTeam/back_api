package io.my.bbang.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import io.my.bbang.comment.domain.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByParentId(String id, Pageable pageable);
    Mono<Long> countByParentId(String id);
    Mono<Long> countByUserIdAndType(String id, String type);
    Mono<Long> countByuserId(String userId);
    Mono<Long> countByTypeAndParentId(String parentId, String type);
}
