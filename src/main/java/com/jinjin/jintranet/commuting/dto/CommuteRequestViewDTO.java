package com.jinjin.jintranet.commuting.dto;

import com.jinjin.jintranet.model.CommutingRequest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommuteRequestViewDTO {

	private Integer id;
	private String type;
	private String requestDt;
	private LocalDateTime crtDt;
	private String name;

	private String approveName;

	private String status;

	@Builder
	public CommuteRequestViewDTO(CommutingRequest commutingRequest) {
		this.id = commutingRequest.getId();
		this.type = commutingRequest.getType();
		this.requestDt = commutingRequest.getRequestDt();
		this.crtDt = commutingRequest.getCrtDt();
		this.name = commutingRequest.getMember().getName();
		this.approveName = commutingRequest.getApprove().getName();
		this.status = commutingRequest.getStatus();
	}
}
