package com.jinjin.jintranet.commuting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommuteRequestInsertDTO {
	
	private Integer approveId;
	
	private String content;
	
	private String requestDt;
	
	private String requestTm;

	private String type;

	@Builder
	public CommuteRequestInsertDTO(Integer approveId, String content, String requestDt, String requestTm, String type) {
		this.approveId = approveId;
		this.content = content;
		this.requestDt = requestDt;
		this.requestTm = requestTm;
		this.type = type;
	}
	
}
