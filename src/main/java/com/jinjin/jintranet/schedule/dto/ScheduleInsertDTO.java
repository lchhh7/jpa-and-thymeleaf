package com.jinjin.jintranet.schedule.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.model.Schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInsertDTO {
	
	@NotBlank(message = "일정 종류를 선택해주세요.")
	private String type;
	
	@Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 일정 시작일을 선택해주세요. (yyyy-MM-dd)")
	private String strDt;
	
	@Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 일정 종료일 선택해주세요. (yyyy-MM-dd)")
	private String endDt;
	
	@Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 시작시간을 입력해주세요. (HH:mm)")
	private String startTm;
	
	@Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 종료시간을 입력해주세요.  (HH:mm)")
	private String endTm;
	
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	
	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
	
	private String color;
	
	private Integer approveId;
	
	private String vacationType;
	
	
	
	/*public Schedule DTOtoEntity() {
		Schedule schedule = new Schedule().builder()
				.type(this.getType())
				.strDt(DateUtils.toLocalDateTime(this.getStrDt() , this.getStartTm()))
				.endDt(DateUtils.toLocalDateTime(this.getEndDt() , this.getEndTm()))
				.title(this.getTitle())
				.content(this.getContent())
				.color(this.color)
				.approveId(this.getApproveId())
				.build();
		
		return schedule;
	}*/

}
