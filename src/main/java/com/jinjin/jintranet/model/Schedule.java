package com.jinjin.jintranet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.schedule.dto.ScheduleInsertDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Member member;
	
	@Column(nullable = false , length = 100)
	private String title;
	
	@Column(nullable = false , length = 100)
	private String content;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
	private LocalDateTime strDt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
	private LocalDateTime endDt;
	
	@OneToOne
	@JoinColumn(name = "approveId")
	private Member approve;
	
	private LocalDate approveDt;
	
	private String status;
	
	private String cancelReason;
	
	private String color;

	public Schedule(Integer id) {
		this.id = id;
	}
	public Schedule(ScheduleInsertDTO dto , Member approve) {
		this.type = dto.getType();
		this.title = dto.getTitle();
		this.content = dto.getContent();
		this.strDt = DateUtils.toLocalDateTime(dto.getStrDt() , dto.getStartTm());
		this.endDt = DateUtils.toLocalDateTime(dto.getEndDt() , dto.getEndTm());
		this.approve = approve;
		this.color = dto.getColor();
	}
	
	@Builder
	public Schedule(Integer id, String type, Member member, String title, String content, LocalDateTime strDt,
			LocalDateTime endDt, Member approve, LocalDate approveDt, String status, String cancelReason,
			String color) {
		this.id = id;
		this.type = type;
		this.member = member;
		this.title = title;
		this.content = content;
		this.strDt = strDt;
		this.endDt = endDt;
		this.approve = approve;
		this.approveDt = approveDt;
		this.status = status;
		this.cancelReason = cancelReason;
		this.color = color;
	}
	
	
}
