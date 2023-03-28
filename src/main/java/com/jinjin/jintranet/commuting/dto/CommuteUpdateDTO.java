package com.jinjin.jintranet.commuting.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommuteUpdateDTO {
	
	@Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 시간을 입력해주세요. (HH:mm)")
	private String tm;
	
	private Integer approveId;
	
	@NotBlank(message = "사유를 입력해주세요.")
	private String changeReason;
	
	@UpdateTimestamp
	private LocalDateTime udtDt;
	
}
