package io.my.bbang.breadstore.domain;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreMenu {

	@Id
	private String id;
	
	private String menuId;
	
	private String storeId;
	
	private String menuNm;
	
	private String menuPrice;
	
}
