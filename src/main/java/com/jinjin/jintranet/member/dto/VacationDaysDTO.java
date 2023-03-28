package com.jinjin.jintranet.member.dto;

import com.jinjin.jintranet.model.Member;

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

	public VacationDaysDTO(Member m) {
		this.total = m.getTotal();
		this.use = m.getUse();
	}
	
	
}
