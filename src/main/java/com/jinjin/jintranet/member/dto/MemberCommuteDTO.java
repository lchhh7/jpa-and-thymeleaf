package com.jinjin.jintranet.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCommuteDTO {

	private String memberName;

	private Double total;

	private Double use;

	private Integer add;

	private LocalDateTime crtDt;

	
	
}
