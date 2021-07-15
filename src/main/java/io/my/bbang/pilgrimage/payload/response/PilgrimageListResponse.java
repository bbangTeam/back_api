package io.my.bbang.pilgrimage.payload.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import lombok.Data;

@Data
public class PilgrimageListResponse extends BbangResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String userId;
	private List<PilgrimageListDto> storeList;
	
	public PilgrimageListResponse() {
		this.storeList = new ArrayList<>();
	}

}
