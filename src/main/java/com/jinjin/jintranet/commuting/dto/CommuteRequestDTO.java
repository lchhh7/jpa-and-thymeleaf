package com.jinjin.jintranet.commuting.dto;

import com.jinjin.jintranet.model.CommutingRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import javax.transaction.Transactional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommuteRequestDTO {
	
	private String status;
	private String type;
	private String requestDt;
	@Transient
	private Long hours;

	@Transient
	private Long minutes;

	public CommuteRequestDTO(CommutingRequest c) {
		this.status = c.getStatus();
		this.type = c.getType();
		this.requestDt = c.getRequestDt();
	}
}
