package io.my.bbang.breadstore.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "store")
@Getter
@Setter
public class Store {

	@Id
	private String id;

    private String entrpNm;
    private String loadAddr;
    private String cityDoCd;
    private String cityGnGuCd;
    private double xposLo;
    private double yposLa;
    private String areaNm;
    private String homepageUrl;
    private String telNo;
    private String reprsntMenuNm;
    private String menuPc;
    private String baseYmd;
    private String naverPlaceUrl;
    private String businessHours;
    private String naverThumbUrl;
    
    private String storeId;
    
    private StoreMenu storeMenu;
}
