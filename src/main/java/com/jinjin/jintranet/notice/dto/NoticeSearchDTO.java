package com.jinjin.jintranet.notice.dto;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSearchDTO {
	
	private Integer id;
	
	private String title;
	
	private Member member;
	
	private LocalDateTime crtDt;
	
	private Long attachesCnt;
	
	public NoticeSearchDTO(Notice notice) {
		this.id = notice.getId();
		this.title = notice.getTitle();
		this.member = notice.getMember();
		this.crtDt = notice.getCrtDt();
		this.attachesCnt = notice.getAttaches().stream().filter(a -> a.getDeletedBy() ==null).count() ;
	}
}
