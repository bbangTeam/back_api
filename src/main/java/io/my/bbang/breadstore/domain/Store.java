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

    private String entrp_nm;
    private String load_addr;
    private String city_do_cd;
    private String city_gn_gu_cd;
    private double xpos_lo;
    private double ypos_la;
    private String area_nm;
    private String homepage_url;
    private String tel_no;
    private String reprsnt_menu_nm;
    private String menu_pc;
    private String base_ymd;
    private String naver_place_url;
    private String business_hours;
    private String naver_thumb_url;
}
