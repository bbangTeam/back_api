package io.my.bbang.commons.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseTimeEntity {

	@Field("createDate")
	@CreatedDate
	private LocalDateTime createDate;

	@Field("modifyDate")
	@LastModifiedDate
	private LocalDateTime modifyDate;
	
	protected BaseTimeEntity() {
		this.createDate = LocalDateTime.now();
		modifyDate = createDate;
	}

}
