package com.jinjin.jintranet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Member extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false, length =30 , unique = true)
	private String memberId;

	@Column(unique = true)
	private String oauthId;

	private String password;
	
	private String name;
	
	private String phoneNo;
	private String mobileNo;

	@Enumerated(EnumType.STRING)
	private PositionType position;
	
	@Enumerated(EnumType.STRING)
	private DepartmentType department;

	private String useColor;
	
	// 시큐리티 ROLE 용
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	@Transient
	private Double total;
	
	@Transient
	private Double use;
	
	@Transient
	private Integer add;
	
	@Builder
	public Member(String memberId, String password, String name, String phoneNo, String mobileNo,
			PositionType position, DepartmentType department, String useColor, RoleType role) {
		this.memberId = memberId;
		this.password = password;
		this.name = name;
		this.phoneNo = phoneNo;
		this.mobileNo = mobileNo;
		this.position = position;
		this.department = department;
		this.useColor = useColor;
		this.role = role;
	}
	
	public Member update(String name) {
		this.name = name;
		return this;
	}
}