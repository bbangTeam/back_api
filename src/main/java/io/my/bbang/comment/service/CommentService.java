package io.my.bbang.comment.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import io.my.bbang.comment.domain.Comment;
import io.my.bbang.comment.dto.CommentListDto;
import io.my.bbang.comment.payload.response.CommentCountResponse;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.payload.response.CommentWriteResponse;
import io.my.bbang.comment.repository.CommentRepository;
import io.my.bbang.commons.context.ReactiveJwtContextHolder;
import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserService userService;
	private final JwtUtil jwtUtil;
	
	public Mono<CommentListResponse> list(String id, int pageSize, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Direction.DESC, "createDate"));
		return commentRepository.findByParentId(id, pageable).map(entity -> {
			CommentListDto dto = new CommentListDto();
			dto.setContent(entity.getContent());
			dto.setNickname(entity.getNickname());
			return dto;
		}).collectList().map(list -> {
			CommentListResponse responseBody = new CommentListResponse();
			responseBody.setCommentList(list);
			responseBody.setResult("Success");
			return responseBody;
		});
	}
	
	public Mono<CommentWriteResponse> write(String id, String content, String type) {
		return ReactiveJwtContextHolder.getContext()
						.flatMap(jwtContext -> jwtContext.getJwt())
						.map(jwt -> jwtUtil.getUserIdByAccessToken(jwt))
						.flatMap(userId -> userService.findById(userId))
						.map(user -> Comment.build(id, user.getName(), content, type))
						.flatMap(comment -> commentRepository.save(comment))
						.map(comment -> {
							CommentWriteResponse responseBody = new CommentWriteResponse();
							responseBody.setId(comment.getId());
							responseBody.setResult("Success");
							return responseBody;
		});
	}

	public Mono<CommentCountResponse> count(String id) {
		return commentRepository.countByParentId(id).map(count -> {
			CommentCountResponse responseBody = new CommentCountResponse();
			responseBody.setCount(count.intValue());
			responseBody.setResult("Success");
			return responseBody;
		});
	}
	

}
