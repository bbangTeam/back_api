package io.my.bbang.user.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.my.bbang.commons.entity.BaseTimeEntity;
import io.my.bbang.commons.security.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document("user")
public class User extends BaseTimeEntity implements UserDetails {
	
	@Id
	private String id;

	private String email;
	private String imageUrl;

	private String name;
	private String loginId;
	private String password;
	private String nickname;
	
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
		return authorities;
	}

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

}
