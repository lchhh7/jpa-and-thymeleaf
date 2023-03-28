package com.jinjin.jintranet.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Commuting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Member member;
	
	private LocalDateTime commutingTm;
	
	private String attendYn;
	
	@Builder
	public Commuting(Integer id, Member member, LocalDateTime commutingTm, String attendYn) {
		this.id = id;
		this.member = member;
		this.commutingTm = commutingTm;
		this.attendYn = attendYn;
	}
	
	
}
