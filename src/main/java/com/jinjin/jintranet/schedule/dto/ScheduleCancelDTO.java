package com.jinjin.jintranet.schedule.dto;

import com.jinjin.jintranet.model.Schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCancelDTO {
	
	private Integer id;
	
	private String cancelReason;
	
	public Schedule DTOtoEntity() {
		Schedule schedule = new Schedule().builder()
				.id(this.getId())
				.cancelReason(this.getCancelReason())
				.build();
		
		return schedule;
	}

	public ScheduleCancelDTO(Schedule s) {
		this.id = s.getId();
		this.cancelReason = s.getCancelReason();
	}
	
	
}
