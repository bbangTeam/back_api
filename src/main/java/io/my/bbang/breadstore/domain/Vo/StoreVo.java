package io.my.bbang.breadstore.domain.Vo;

import io.my.bbang.breadstore.domain.Store;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Setter
@Getter
public class StoreVo {

	
	
	
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
   
    public StoreVo(Mono<Store> store) {
    	store.subscribe(s->
    		this.setEntrpNm(s.getEntrpNm())
    	);
    }

	public StoreVo() {
		// TODO Auto-generated constructor stub
	}
    

}
