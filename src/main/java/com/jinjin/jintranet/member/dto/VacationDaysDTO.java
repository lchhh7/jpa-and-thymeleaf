package com.jinjin.jintranet.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacationDaysDTO {
	
	private double total;
	
	private double use;

	private double add;

	public VacationDaysDTO(MemberCommuteDTO m) {
		this.total = m.getTotal();
		this.use = m.getUse();
		this.add = m.getAdd();
	}
	
	
}
