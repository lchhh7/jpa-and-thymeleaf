package com.jinjin.jintranet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

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

	public Holiday(String title, LocalDate holidayDt) {
		this.title = title;
		this.holidayDt = holidayDt;
	}
	@Builder
	public Holiday(Integer id, String title, LocalDate holidayDt) {
		this.id = id;
		this.title = title;
		this.holidayDt = holidayDt;
	}
	
	

}
