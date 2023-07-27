package com.jinjin.jintranet.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CommutingRequest extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String type;
	
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Member member;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "Asia/Seoul")
	private String requestDt;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss" , timezone = "Asia/Seoul")
	private String requestTm;
	
	private String content;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approveId")
	private Member approve;
	
	private LocalDateTime approveDt;

	@Builder
	public CommutingRequest(Integer id, String type, String status, Member member, String requestDt,
			String requestTm, String content, Member approve, LocalDateTime approveDt) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.member = member;
		this.requestDt = requestDt;
		this.requestTm = requestTm;
		this.content = content;
		this.approve = approve;
		this.approveDt = approveDt;
	}

/*
	public void changeMember(Member member) {
		this.member = member;
		member.getCommutingRequests().add(this);
	}
*/


	
}
