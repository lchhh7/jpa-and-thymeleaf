package com.jinjin.jintranet.notice.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.jinjin.jintranet.model.Notice;
import com.jinjin.jintranet.model.NoticeAttach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeAttachDTO {
	
	private Integer id;
	
	private String path;
	
	private String originalFileName;
	
	private String storedFileName;
	
	private Long fileSize;
	
	public NoticeAttachDTO(NoticeAttach attach) {
		//this.notice = new NoticeSaveDTO(attach.getNotice());
		this.id = attach.getId();
		this.path = attach.getPath();
		this.originalFileName = attach.getOriginalFileName();
		this.storedFileName = attach.getStoredFileName();
		this.fileSize = attach.getFileSize();
	}

	public NoticeAttach DtotoEntity() {
		NoticeAttach attach = new NoticeAttach();
		attach.setPath(this.getPath());
		attach.setOriginalFileName(this.getOriginalFileName());
		attach.setStoredFileName(this.getStoredFileName());
		attach.setFileSize(this.getFileSize());
		return attach;
	}
	
	
}
