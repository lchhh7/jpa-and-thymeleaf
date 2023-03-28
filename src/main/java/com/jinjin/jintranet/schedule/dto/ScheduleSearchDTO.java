package com.jinjin.jintranet.schedule.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinjin.jintranet.model.Schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSearchDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String type;
	
	@Column(nullable = false , length = 100)
	private String title;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
	private LocalDateTime strDt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
	private LocalDateTime endDt;
	
	private String status;
	
	private String color;

	public ScheduleSearchDTO(Schedule s) {
		this.id = s.getId();
		this.type = s.getType();
		this.title = s.getTitle();
		this.strDt = s.getStrDt();
		this.endDt = s.getEndDt();
		this.status = s.getStatus();
		this.color = s.getColor();
	}
	
	
	
}
