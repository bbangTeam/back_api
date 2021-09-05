package io.my.bbang.comment.service;

import java.util.Comparator;
import java.util.List;

import io.my.bbang.breadstagram.repository.BreadstagramRepository;
import io.my.bbang.comment.dto.CommentType;
import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.pilgrimage.repository.PilgrimageBoardRepository;
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
	private final PilgrimageBoardRepository pilgrimageBoardRepository;
	private final BreadstagramRepository breadstagramRepository;

	private final JwtUtil jwtUtil;
	
	public Mono<CommentListResponse> list(String id, int pageSize, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Direction.DESC, "createDate"));
		
		return commentRepository.findByParentId(id, pageable)
		.flatMap(this::returnDto).collectList()
		.map(list -> {
			list.sort(Comparator.comparing(CommentListDto::getCreateDate).reversed());
			return returnResponse(list);
		});
	}

	private Mono<CommentListDto> returnDto(Comment entity) {
		CommentListDto dto = new CommentListDto();
		dto.setContent(entity.getContent());
		dto.setLikeCount(entity.getLikeCount());
		dto.setReCommentCount(entity.getReCommentCount());
		dto.setClickCount(entity.getClickCount());
		dto.setCreateDate(entity.getCreateDate());
		dto.setModifyDate(entity.getModifyDate());

		return userRepository.findById(entity.getUserId()).flatMap(user -> {
			dto.setNickname(user.getNickname());
			return userHeartRepository.findByUserIdAndParentIdAndType(
					user.getId(), entity.getParentId(), UserHeartType.COMMENT.getValue())
					.switchIfEmpty(Mono.empty());
		})
		.flatMap(userHeart -> Mono.just(true))
		.switchIfEmpty(Mono.just(false))
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
	
	public Mono<BbangResponse> write(String id, String content, String type, String parentId) {
		return jwtUtil.getMonoUserId().flatMap(userId ->
			userRepository.findById(userId)
					.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)))
					.flatMap(user -> {
						Comment comment = Comment.build(id, user.getId(), content, type);
						return commentRepository.save(comment).switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)));
					})
					.flatMap(e -> this.commentCountPlus(type, parentId)))
				.map(e -> new BbangResponse());
	}

	public Mono<Object> commentCountPlus(String type, String parentId) {
		if (parentId != null && parentId.equals("")) {
			if (CommentType.RE_COMMENT.equalsType(type)) {
				return commentRepository.findById(parentId).flatMap(entity -> {
					entity.setReCommentCount(entity.getReCommentCount() + 1);
					return commentRepository.save(entity);
				});
			} else if (CommentType.PILGRIMAGE.equalsType(type)) {
				return pilgrimageBoardRepository.findById(parentId).flatMap(entity -> {
					entity.setCommentCount(entity.getCommentCount() + 1);
					return pilgrimageBoardRepository.save(entity);
				});
			} else if (CommentType.BREADSTAGRAM.equalsType(type)) {
				return breadstagramRepository.findById(parentId).flatMap(entity -> {
					entity.setCommentCount(entity.getCommentCount() + 1);
					return breadstagramRepository.save(entity);
				});
			}
		}
		return Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION));
	}

	public Mono<BbangResponse> delete(String id) {
		return jwtUtil.getMonoUserId().flatMap(userId ->
			commentRepository.findById(id)
				.switchIfEmpty(Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION)))
				.flatMap(entity -> {
					String type = entity.getType();
					String parentId = entity.getParentId();

					if (CommentType.RE_COMMENT.equalsType(type)) {
						return commentRepository.findById(parentId).flatMap(e -> {
							e.setReCommentCount(e.getReCommentCount() - 1);
							return commentRepository.save(e).map(e1 -> entity);
						});
					} else if (CommentType.PILGRIMAGE.equalsType(type)) {
						return pilgrimageBoardRepository.findById(parentId).flatMap(e -> {
							e.setCommentCount(e.getCommentCount() - 1);
							return pilgrimageBoardRepository.save(e).map(e1 -> entity);
						});
					} else if (CommentType.BREADSTAGRAM.equalsType(type)) {
						return breadstagramRepository.findById(parentId).flatMap(e -> {
							e.setCommentCount(e.getCommentCount() - 1);
							return breadstagramRepository.save(e).map(e1 -> entity);
						});
					}
					return Mono.error(new BbangException(ExceptionTypes.DATABASE_EXCEPTION));
				})
				.flatMap(commentRepository::delete).map(e -> new BbangResponse()))
			;

	}

}
