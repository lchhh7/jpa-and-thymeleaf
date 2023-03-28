package com.jinjin.jintranet.commuting.dto;

import java.time.LocalDateTime;

public interface CommutingsInterface {
	
	Integer getId();
	
	LocalDateTime getCommutingTm();
	
	String getAttendYn();
	
	Integer getCnt();
}
