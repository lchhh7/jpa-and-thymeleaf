package com.jinjin.jintranet.notice.dto;

import com.jinjin.jintranet.model.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSaveDTO {
	
	private Integer id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String content;
	
	private List<NoticeAttachDTO> attaches;
	
	
	public NoticeSaveDTO(Notice notice) {
		this.id = notice.getId();
		this.title = notice.getTitle();
		this.content = notice.getContent();
		//this.attaches = notice.getAttaches().stream().map(m -> new NoticeAttachDTO(m)).toList();
		//this.attaches = notice.getAttaches();
	}
	
}
