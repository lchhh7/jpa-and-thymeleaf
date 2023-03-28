package com.jinjin.jintranet.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Holiday {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String title;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate holidayDt;
	
	@Builder
	public Holiday(Integer id, String title, LocalDate holidayDt) {
		this.id = id;
		this.title = title;
		this.holidayDt = holidayDt;
	}
	
	

}
