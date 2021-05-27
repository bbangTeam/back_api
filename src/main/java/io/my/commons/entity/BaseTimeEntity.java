package io.my.commons.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseTimeEntity {

	@Field("createdTime")
	@CreatedDate
	private LocalDateTime createdTime;

	@Field("updatedTime")
	@LastModifiedDate
	private LocalDateTime updatedTime;
	
	protected BaseTimeEntity() {
    	this.setCreatedTime(LocalDateTime.now());
    	this.setUpdatedTime(LocalDateTime.now());
	}

}
