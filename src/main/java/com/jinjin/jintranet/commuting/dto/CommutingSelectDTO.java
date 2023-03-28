package com.jinjin.jintranet.commuting.dto;

import java.time.LocalDateTime;

import com.jinjin.jintranet.model.Commuting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommutingSelectDTO {

	private Integer id;
	
	private LocalDateTime commutingTm;
	
	private String attendYn;

	public CommutingSelectDTO(Commuting c) {
		this.id = c.getId();
		this.commutingTm = c.getCommutingTm();
		this.attendYn = c.getAttendYn();
	}
	
	
}
