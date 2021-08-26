package io.my.bbang.ideal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.my.bbang.commons.utils.JwtUtil;
import io.my.bbang.user.domain.UserIdeal;
import io.my.bbang.user.repository.UserIdealRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.ideal.domain.Ideal;
import io.my.bbang.ideal.dto.IdealContentDto;
import io.my.bbang.ideal.dto.IdealRankDto;
import io.my.bbang.ideal.payload.response.IdealContentResponse;
import io.my.bbang.ideal.payload.response.IdealRankResponse;
import io.my.bbang.ideal.repository.IdealRepository;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdealService {
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final IdealRepository idealRepository;
	private final UserIdealRepository userIdealRepository;

	public Mono<BbangResponse> done() {
		return findUserIdealByUserId().hasElement().map(bool -> {
			if (bool) {
				return new BbangResponse(21, "Clear game!");
			} else {
				return new BbangResponse();
			}
		});
	}
	
	public Mono<IdealContentResponse> content() {
		return findUserIdealByUserId().hasElement().flatMap(bool -> {
			if (bool){
				return Mono.just(returnResponse(21, "Clear game!"));
			} else {
				return idealRepository.findAll().collectList().map(entityList -> {
					IdealContentResponse responseBody = new IdealContentResponse();
					responseBody.setBreadList(entityListToDtoList(entityList));
					return responseBody;
				});
			}
		});
	}

	public Mono<UserIdeal> findUserIdealByUserId() {
		return jwtUtil.getMonoUserId().flatMap(userIdealRepository::findByuserId);
	}

	private IdealContentResponse returnResponse(int code, String result) {
		IdealContentResponse responseBody = new IdealContentResponse();
		responseBody.setCode(code);
		responseBody.setResult(result);
		return responseBody;
	}

	private List<IdealContentDto> entityListToDtoList(List<Ideal> entityList) {
		Collections.shuffle(entityList);
		if (entityList.size() < 32) throw new BbangException(ExceptionTypes.DATABASE_EXCEPTION);

		List<IdealContentDto> dtoList = new ArrayList<>();
		for (int index=0; index<32; index++) {
			Ideal entity = entityList.get(index);
			dtoList.add(entityToContentDto(entity));
		}
		return dtoList;
	}

	private IdealContentDto entityToContentDto(Ideal entity) {
		return new IdealContentDto(
			entity.getId(), 
			entity.getBreadName(), 
			entity.getImageUrl()
		);
	}

	public Mono<IdealRankResponse> rank() {
		int pageNum = 0;
		int pageSize = 5;

		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Direction.DESC, "selectedCount"));

		return idealRepository.findByIdNotNull(pageable).collectList().map(entityList -> {
			IdealRankResponse responseBody = new IdealRankResponse();
			entityList.forEach(entity -> responseBody.getBreadList().add(entityToRankDto(entity)));
			return responseBody;
		});
	}

	private IdealRankDto entityToRankDto(Ideal entity) {
		return new IdealRankDto(
			entity.getBreadName(), 
			entity.getImageUrl(), 
			entity.getSelectedCount()
		);

	}


	public Mono<BbangResponse> selected(String id) {
		return userService.saveUserIdeal(id)
		.flatMap(entity -> idealRepository.findById(id))
		.flatMap(entity -> {
			entity.setSelectedCount(
				entity.getSelectedCount() == null 
				? 0
				: entity.getSelectedCount() + 1
			);
			return idealRepository.save(entity);
		}).map(entity -> new BbangResponse())
		;
	}

}
