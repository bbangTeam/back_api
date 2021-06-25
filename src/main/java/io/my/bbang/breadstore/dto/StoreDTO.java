package io.my.bbang.breadstore.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.my.bbang.breadstore.domain.Store;
import io.my.bbang.breadstore.domain.StoreMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO implements Serializable{
	
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
    
    private List<StoreMenu> storeMenu;
    
	public StoreDTO(Mono<Store> store, Flux<StoreMenu> storeMenuValue) {
		
		List<StoreMenu> storeMenuList=  storeMenuValue.collectList().block();
		
		this.setEntrpNm(store.block().getEntrpNm());
		this.setLoadAddr(store.block().getLoadAddr());
		this.setXposLo(store.block().getXposLo());
		this.setYposLa(store.block().getYposLa());
		this.setAreaNm(store.block().getAreaNm());
		this.setHomepageUrl(store.block().getHomepageUrl());
		this.setTelNo(store.block().getTelNo());
		this.setReprsntMenuNm(store.block().getReprsntMenuNm());
		this.setMenuPc(store.block().getMenuPc());
		this.setBaseYmd(store.block().getBaseYmd());
		this.setBusinessHours(store.block().getBusinessHours());
		this.setNaverThumbUrl(store.block().getNaverThumbUrl());
		this.setStoreMenu(storeMenuList); 
	}

	public StoreDTO(Store val) {
		this.setEntrpNm(val.getEntrpNm());
	}

	public StoreDTO(Flux<Store> storeList) {
		Store store= storeList.share().blockFirst();
		this.setAreaNm(store.getAreaNm());
	}


}
