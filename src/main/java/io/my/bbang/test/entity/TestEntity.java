package io.my.bbang.test.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.my.commons.entity.BaseTimeEntity;
import io.my.commons.security.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document(value="test")
public class TestEntity extends BaseTimeEntity {
	
    @Id
    private String id;

    @Field("name")
    private String name;
    
    @Field("login_id")
    private String loginId;
    @Field("password")
    private String password;
    
    @Field("roles")
	private List<UserRole> roles;
    
    private TestEntity(String loginId, String password) {
    	super();
    	this.loginId = loginId;
    	this.password = password;
    	this.roles = new ArrayList<>();
    }
    
    public static TestEntity newInstance(String loginId, String password) {
    	return new TestEntity(loginId, password);
    }
}
