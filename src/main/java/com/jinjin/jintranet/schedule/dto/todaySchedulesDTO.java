package com.jinjin.jintranet.schedule.dto;

import java.time.LocalDateTime;

import com.jinjin.jintranet.model.Schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class todaySchedulesDTO {
	
	private LocalDateTime strDt;
	
	private LocalDateTime endDt;
	
	private String title;
	
	private String type;
	
	private String status;

	public todaySchedulesDTO(Schedule s) {
		this.strDt = s.getStrDt();
		this.endDt = s.getEndDt();
		this.title = s.getTitle();
		this.type = s.getType();
		this.status = s.getStatus();
	}
	
	
}
