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
public class MemberSearchDTO {
	
	private Integer id;
	
	private String name;
	
	private String memberId;
	
	private PositionType position;
	
	private DepartmentType department;
	
	private String phoneNo;
	
	private String mobileNo;
	
	
	public MemberSearchDTO(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.memberId = member.getMemberId();
		this.position = member.getPosition();
		this.department = member.getDepartment();
		this.phoneNo = member.getPhoneNo();
		this.mobileNo = member.getMobileNo();
	}

	public MemberSearchDTO(Integer id, String name, String memberId, PositionType position, DepartmentType department,
			String phoneNo, String mobileNo) {
		this.id = id;
		this.name = name;
		this.memberId = memberId;
		this.position = position;
		this.department = department;
		this.phoneNo = phoneNo;
		this.mobileNo = mobileNo;
	}
	
	
}
