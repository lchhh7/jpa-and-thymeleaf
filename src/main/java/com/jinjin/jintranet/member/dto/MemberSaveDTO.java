package com.jinjin.jintranet.member.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;
import com.jinjin.jintranet.model.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveDTO {
	
	private Integer id;
	
	@NotBlank
	private String name;
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$", message = "올바른 형식을 입력해주세요.(6자리 이상, 1개이상의 문자, 특수문자 이용)")
	private String password;
	
	@Column(nullable = false, length =30 , unique = true)
	private String memberId;
	
	private PositionType position;
	
	private DepartmentType department;
	
	private String phoneNo;
	
	private String mobileNo;
	
	private String useColor;
	
	private RoleType role;
	
	public MemberSaveDTO(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.password = member.getPassword();
		this.memberId = member.getMemberId();
		this.position = member.getPosition();
		this.department = member.getDepartment();
		this.phoneNo = member.getPhoneNo();
		this.mobileNo = member.getMobileNo();
		this.useColor = member.getUseColor();
		this.role = member.getRole();
	}
	
	public Member DtoToEntity() {
		Member member = new Member(this.getMemberId() ,this.getPassword() ,this.getName() ,
				this.getPhoneNo() , this.getMobileNo() , this.getPosition() , this.getDepartment() ,
				this.getUseColor() , this.getRole());
		return member;
	}
	
}
