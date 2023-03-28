package com.jinjin.jintranet.member.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;
import com.jinjin.jintranet.model.RoleType;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.dto.ScheduleViewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDefaultDTO {
	
	private Integer id;
	
	private String name;
	
	private String memberId;
	
	private PositionType position;
	
	private DepartmentType department;
	
	private String phoneNo;
	
	private String useColor;
	
	private RoleType role;
	
	public MemberDefaultDTO(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.memberId = member.getMemberId();
		this.position = member.getPosition();
		this.department = member.getDepartment();
		this.phoneNo = member.getPhoneNo();
		this.useColor = member.getUseColor();
		this.role = member.getRole();
	}
	
	
}
