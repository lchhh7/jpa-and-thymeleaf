package com.jinjin.jintranet.member.dto;

import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditDTO {
	
	private String name;
	
	private PositionType position;
	
	private DepartmentType department;
	
	private String phoneNo;
	
	private String mobileNo;
	
	private String useColor;
	
	public MemberEditDTO(Member member) {
		this.name = member.getName();
		this.position = member.getPosition();
		this.department = member.getDepartment();
		this.phoneNo = member.getPhoneNo();
		this.mobileNo = member.getMobileNo();
		this.useColor = member.getUseColor();
	}
	
}
