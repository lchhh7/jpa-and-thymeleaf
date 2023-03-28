package com.jinjin.jintranet.member.dto;

import java.util.List;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberViewDTO {
	
	private Integer id;
	
	private String name;
	
	public MemberViewDTO(Member member) {
		this.id = member.getId();
		this.name = member.getName();
	}
	
	
}
