package com.jinjin.jintranet.member.dto;

import javax.validation.constraints.Pattern;

import com.jinjin.jintranet.model.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEditDTO {
	
	private Integer id;
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$", message = "올바른 형식을 입력해주세요.(6자리 이상, 1개이상의 문자, 특수문자 이용)")
	private String password;
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$", message = "올바른 형식을 입력해주세요.(6자리 이상, 1개이상의 문자, 특수문자 이용)")
	private String newPassword;
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$", message = "올바른 형식을 입력해주세요.(6자리 이상, 1개이상의 문자, 특수문자 이용)")
	private String newPassword2;
	
	public PasswordEditDTO(Member member) {
		this.id = member.getId();
		this.password = member.getPassword();
	}
}
