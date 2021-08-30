package io.my.bbang.breadstore.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    private long likeCount;
    private long clickCount;
    private double star;
    private long starCount;
    private long starSum;
    private long reviewCount;

    private List<Integer> bakeTimeList;

    private StoreMenu storeMenu;
}
