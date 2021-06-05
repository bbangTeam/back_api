package io.my.bbang.pilgrimage.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.pilgrimage.dto.PilgrimageListDto;
import lombok.Data;

@Data
public class PilgrimageListResponse {
	private String result;
	private String cityName;
	private List<PilgrimageListDto> storeList;
	
	public PilgrimageListResponse() {
		this.storeList = new ArrayList<>();
	}

}
