package com.jinjin.jintranet.commuting.dto;

import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
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
	private Member member;
	private String type;
	private String requestDt;

	private String requestTm;
	private String content;
	private String status;

	private String approveName;
	private LocalDateTime approveDt;

	@Builder
	public AdminCommuteRequestViewDTO(CommutingRequest commutingRequest) {
		this.id = commutingRequest.getId();
		this.member = commutingRequest.getMember();
		this.type = commutingRequest.getType();
		this.requestDt = commutingRequest.getRequestDt();
		this.requestTm = commutingRequest.getRequestTm();
		this.content = commutingRequest.getContent();
		this.status = commutingRequest.getStatus();
		this.approveName = commutingRequest.getApprove().getName();
		this.approveDt = commutingRequest.getApproveDt();
	}
}
