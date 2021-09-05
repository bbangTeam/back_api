package io.my.bbang.comment.controller;

import io.my.bbang.comment.dto.CommentType;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.payloads.BbangResponse;
import org.springframework.web.bind.annotation.*;

import io.my.bbang.comment.payload.request.CommentWriteRequest;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
	private final CommentService commentService;

	@GetMapping("/list")
	public Mono<CommentListResponse> list(
			@RequestParam String id, 
			@RequestParam(defaultValue = "0", required = false) int pageNum,
			@RequestParam(defaultValue = "5", required = false) int pageSize) {
		return commentService.list(id, pageSize, pageNum);
	}
	
	@PostMapping
	public Mono<BbangResponse> write(@RequestBody CommentWriteRequest requestBody) {
		String id = requestBody.getId();
		String content = requestBody.getContent();
		String type = requestBody.getType();
		String parentId = requestBody.getParentId();

		if (!CommentType.isExistType(type)) throw new BbangException(ExceptionTypes.TYPE_EXCEPTION);
		
		return commentService.write(id, content, type, parentId);
	}

	@DeleteMapping
	public Mono<BbangResponse> delete(@RequestParam("id") String id) {
		return commentService.delete(id);
	}
}
