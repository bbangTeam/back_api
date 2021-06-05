package io.my.bbang.user.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.commons.entity.BaseTimeEntity;
import io.my.bbang.commons.security.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "user")
public class User extends BaseTimeEntity {
	
	@Id
	private String id;
	
	private String name;
	private String loginId;
	private String password;
	private List<UserRole> roles;
	
	private User(String loginId, String password) {
		super();
		this.loginId = loginId;
		this.password = password;
		this.roles = new ArrayList<>();
	}
	
	public static User newInstance(String loginId, String password) {
		return new User(loginId, password);
	}

}
