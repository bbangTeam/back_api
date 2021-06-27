package io.my.bbang.comment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.comment.payload.request.CommentWriteRequest;
import io.my.bbang.comment.payload.response.CommentCountResponse;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.payload.response.CommentWriteResponse;
import io.my.bbang.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
	private final CommentService commentService;

	@GetMapping("/list")
	public Mono<CommentListResponse> list(
			@RequestParam String id, 
			@RequestParam String type,
			@RequestParam(defaultValue = "1", required = false) int pageNum,
			@RequestParam int pageSize) {
		return commentService.list(id, pageSize, pageNum);
	}
	
	@PutMapping("/write")
	public Mono<CommentWriteResponse> write(@RequestBody CommentWriteRequest requestBody) {
		String id = requestBody.getId();
		String content = requestBody.getContent();
		
		return commentService.write(id, content);
	}

	@GetMapping(value="/count")
	public Mono<CommentCountResponse> count(@RequestParam String id) {
		return commentService.count(id);
	}
	
}
