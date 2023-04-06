package com.jinjin.jintranet.commuting.dto;

import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommuteApproveDTO {
	private Integer id;
	private String status;
	private String requestDt;
	private Member member;

	@Builder
	public CommuteApproveDTO(Integer id, String status, String requestDt, Member member) {
		this.id = id;
		this.status = status;
		this.requestDt = requestDt;
		this.member = member;
	}
}
