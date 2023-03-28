package com.jinjin.jintranet.commuting.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommuteInsertDTO {
	
	@CreationTimestamp
	private LocalDateTime commutingTm;
	
	private String attendYn;
	
	@Builder
	public CommuteInsertDTO(LocalDateTime commutingTm, String attendYn) {
		this.commutingTm = commutingTm;
		this.attendYn = attendYn;
	}
	
	
	
}
