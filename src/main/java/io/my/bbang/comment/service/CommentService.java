package io.my.bbang.comment.service;

import java.util.List;

import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.comment.dto.CommentType;
import io.my.bbang.pilgrimage.repository.PilgrimageRepository;
import io.my.bbang.user.domain.UserHeart;
import io.my.bbang.user.dto.UserHeartType;
import io.my.bbang.user.repository.UserHeartRepository;
import io.my.bbang.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import io.my.bbang.comment.domain.Comment;
import io.my.bbang.comment.dto.CommentListDto;
import io.my.bbang.comment.payload.response.CommentListResponse;
import io.my.bbang.comment.payload.response.CommentWriteResponse;
import io.my.bbang.comment.repository.CommentRepository;
import io.my.bbang.commons.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;

	private final UserRepository userRepository;
	private final UserHeartRepository userHeartRepository;
	private final PilgrimageRepository pilgrimageRepository;
	private final BreadstagramRepository breadstagramRepository;

	private final JwtUtil jwtUtil;
	
	public Mono<CommentListResponse> list(String id, int pageSize, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Direction.DESC, "createDate"));
		
		return commentRepository.findByParentId(id, pageable)
		.flatMap(this::returnDto).collectList()
		.map(this::returnResponse);
	}

	private Mono<CommentListDto> returnDto(Comment entity) {
		CommentListDto dto = new CommentListDto();
		dto.setContent(entity.getContent());
		dto.setNickname(entity.getNickname());
		dto.setLikeCount(entity.getLikeCount());
		dto.setReCommentCount(entity.getReCommentCount());
		dto.setClickCount(entity.getClickCount());

		return jwtUtil.getMonoUserId().flatMap(userId -> {
			UserHeart userHeart = new UserHeart();
			userHeart.setUserId(userId);
			userHeart.setParentId(entity.getParentId());
			userHeart.setType(UserHeartType.COMMENT.getValue());
			return userHeartRepository.findByUserIdAndParentIdAndType(userHeart);
		})
		.flatMap(userHeart -> Mono.just(true))
		.defaultIfEmpty(false)
		.map(bool -> {
			dto.setLike(bool);
			return dto;
		});
	}

	private CommentListResponse returnResponse(List<CommentListDto> list) {
		CommentListResponse responseBody = new CommentListResponse();
		responseBody.setCommentList(list);
		responseBody.setResult("Success");
		return responseBody;
	}
	
	public Mono<CommentWriteResponse> write(String id, String content, String type, String parentId) {
		commentCountPlus(type, parentId);

		return jwtUtil.getMonoUserId().flatMap(userRepository::findById)
						.map(user -> Comment.build(id, user.getId(), user.getName(), content, type))
						.flatMap(commentRepository::save)
						.map(this::returnResponse);
	}

	public void commentCountPlus(String type, String parentId) {
		if (parentId != null && parentId.equals("")) {
			if (CommentType.RE_COMMENT.equalsType(type)) {
				commentRepository.findById(parentId).subscribe(entity -> {
					entity.setReCommentCount(entity.getReCommentCount() + 1);
					commentRepository.save(entity).subscribe();
				});
			} else if (CommentType.PILGRIMAGE.equalsType(type)) {
				pilgrimageRepository.findById(parentId).subscribe(entity -> {
					entity.setCommentCount(entity.getCommentCount() + 1);
					pilgrimageRepository.save(entity).subscribe();
				});
			} else if (CommentType.BREADSTAGRAM.equalsType(type)) {
				breadstagramRepository.findById(parentId).subscribe(entity -> {
					entity.setCommentCount(entity.getCommentCount() + 1);
					breadstagramRepository.save(entity).subscribe();
				});
			}
		}
	}

	private CommentWriteResponse returnResponse(Comment entity) {
		CommentWriteResponse responseBody = new CommentWriteResponse();
		responseBody.setId(entity.getId());
		responseBody.setResult("Success");
		return responseBody;
	}

}
