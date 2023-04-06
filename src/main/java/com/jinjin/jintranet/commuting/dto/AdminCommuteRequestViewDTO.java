package com.jinjin.jintranet.commuting.dto;

import com.jinjin.jintranet.model.CommutingRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminCommuteRequestViewDTO {

	private Integer id;
	private String name;
	private String type;
	private String requestDt;
	private String content;
	private String status;

	@Builder
	public AdminCommuteRequestViewDTO(CommutingRequest commutingRequest) {
		this.id = commutingRequest.getId();
		this.name = commutingRequest.getApprove().getName();
		this.type = commutingRequest.getType();
		this.requestDt = commutingRequest.getRequestDt();
		this.content = commutingRequest.getContent();
		this.status = commutingRequest.getStatus();
	}
}
