package com.jinjin.jintranet.member.dto;

import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;
import com.jinjin.jintranet.model.RoleType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberShowDTO {
	
	private Integer id;
	
	private String name;
	
	private String memberId;
	
	private PositionType position;
	
	private DepartmentType department;
	
	private String phoneNo;
	
	private String mobileNo;
	
	private String useColor;
	
	private RoleType role;
	
	public MemberShowDTO(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.memberId = member.getMemberId();
		this.position = member.getPosition();
		this.department = member.getDepartment();
		this.phoneNo = member.getPhoneNo();
		this.mobileNo = member.getMobileNo();
		this.useColor = member.getUseColor();
		this.role = member.getRole();
	}
	
}
