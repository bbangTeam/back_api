package io.my.bbang.comment.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.my.bbang.comment.dto.CommentListDto;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.payload.response.CommentWriteResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CommentService {
	
	public Mono<CommentListResponse> list(String id, int pageSize, int pageNum) {
		CommentListResponse responseBody = new CommentListResponse();
		
		responseBody.setResult("Success");
		
		for (int index=0; index<pageSize; index++) {
			CommentListDto dto = new CommentListDto();
			dto.setNickname("빵터짐" + index);
			dto.setContent("맛있어보여요." + index);
			
			responseBody.getCommentList().add(dto);
		}
		
		return Mono.just(responseBody);
	}
	
	public Mono<CommentWriteResponse> write(String id, String content) {
		CommentWriteResponse responseBody = new CommentWriteResponse();
		
		responseBody.setResult("Success");
		responseBody.setId(UUID.randomUUID().toString());
		
		return Mono.just(responseBody);
	}
	

}
