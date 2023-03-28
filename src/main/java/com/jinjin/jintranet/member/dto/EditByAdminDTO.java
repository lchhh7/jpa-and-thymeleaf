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
public class EditByAdminDTO {
	
	private Integer id;
	
	@NotBlank
	private String name;
	
	@Column(nullable = false, length =30 , unique = true)
	private String memberId;
	
	private PositionType position;
	
	private DepartmentType department;
	
	private String phoneNo;
	
	@NotBlank
	private String mobileNo;
	
	private String useColor;
	
	private RoleType role;
	
	public EditByAdminDTO(Member member) {
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
