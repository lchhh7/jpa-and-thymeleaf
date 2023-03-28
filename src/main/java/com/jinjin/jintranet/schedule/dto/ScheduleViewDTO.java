package com.jinjin.jintranet.schedule.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;

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
public class ScheduleViewDTO {
	private Integer id;
	private String type;
	private Member member;
	private String typeName;

	private LocalDateTime strDt;
	private LocalDateTime endDt;
	
	private String title;
	private String content;
	
	private Member approve;
	private LocalDate approveDt;
	
	private String status;
	
	private String color;
	
	private String cancelReason;

	public ScheduleViewDTO(Schedule s) {
		this.id = s.getId();
		this.type = s.getType();
		this.member = s.getMember();
		this.strDt = s.getStrDt();
		this.endDt = s.getEndDt();
		this.title = s.getTitle();
		this.content = s.getContent();
		this.approve = s.getApprove();
		this.approveDt = s.getApproveDt();
		this.status = s.getStatus();
		this.color = s.getColor();
		this.cancelReason = s.getCancelReason();
	}
}
