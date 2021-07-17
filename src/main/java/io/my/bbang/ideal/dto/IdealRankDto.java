package io.my.bbang.ideal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdealRankDto {
    private String name;
    private String imageUrl;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long selectedCount;
    
}
