package com.jinjin.jintranet.commuting.dto;

import com.jinjin.jintranet.model.CommutingRequest;

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
public class CommuteRequestDTO {
	
	private String status;

	private String type;
	private String requestDt;
	
	//private String requestTm;

	
	public CommuteRequestDTO(CommutingRequest c) {
		this.status = c.getStatus();
		this.type = c.getType();
		this.requestDt = c.getRequestDt();
	}
}
